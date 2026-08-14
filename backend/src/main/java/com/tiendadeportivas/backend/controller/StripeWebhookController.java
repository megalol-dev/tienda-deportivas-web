package com.tiendadeportivas.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.model.checkout.Session;
import com.tiendadeportivas.backend.service.PedidoService;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    private final PedidoService pedidoService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public StripeWebhookController(
            PedidoService pedidoService) {

        this.pedidoService = pedidoService;
    }

    // =====================================================
    // SECRETO DEL WEBHOOK
    // -----------------------------------------------------
    // Spring obtiene este valor desde:
    //
    // stripe.webhook-secret=${STRIPE_WEBHOOK_SECRET}
    //
    // Nunca guardamos el secreto directamente en Java.
    // =====================================================

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    // =====================================================
    // RECIBIR EVENTOS DE STRIPE
    // =====================================================

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {

        try {

            // =============================================
            // VERIFICAR QUE EL MENSAJE VIENE DE STRIPE
            // =============================================

            Event event = Webhook.constructEvent(
                    payload,
                    signature,
                    webhookSecret);

            // =============================================
            // MOSTRAR EVENTO RECIBIDO
            // Temporalmente nos servirá para las pruebas.
            // =============================================

            System.out.println(
                    "Evento recibido desde Stripe: "
                            + event.getType());

                            // =============================================
            // PROCESAR PAGO COMPLETADO
            // ---------------------------------------------
            // Solo nos interesa confirmar el pedido cuando
            // Stripe informa de que el Checkout terminó.
            // =============================================

            if ("checkout.session.completed".equals(event.getType())) {

                // =========================================
                // EXTRAER CHECKOUT SESSION DEL EVENTO
                // =========================================

                Session session = (Session) event
                        .getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);

                if (session == null) {

                    System.err.println(
                            "No se pudo obtener la sesión de Stripe.");

                    return ResponseEntity
                            .badRequest()
                            .body("No se pudo obtener la sesión.");
                }

                // =========================================
                // OBTENER NUESTRO ID DE PEDIDO
                // -----------------------------------------
                // Lo guardamos previamente en metadata
                // al crear la Checkout Session.
                // =========================================

                String idPedido = session
                        .getMetadata()
                        .get("idPedido");

                String stripeSessionId = session.getId();

                // =========================================
                // CONFIRMAR EL PAGO EN MARIADB
                // =========================================

                pedidoService.confirmarPagoStripe(
                        stripeSessionId,
                        idPedido);

                System.out.println(
                        "Pago confirmado correctamente: "
                                + idPedido);
            }

            // =============================================
            // RESPONDER 200 A STRIPE
            // =============================================

            return ResponseEntity.ok("Webhook recibido.");

        } catch (SignatureVerificationException e) {

            // =============================================
            // FIRMA INCORRECTA
            // -------------------------------------------------
            // Si alguien intenta llamar manualmente al endpoint
            // sin una firma válida de Stripe, lo rechazamos.
            // =============================================

            System.err.println(
                    "Firma del webhook de Stripe no válida.");

            return ResponseEntity
                    .badRequest()
                    .body("Firma de Stripe no válida.");
        }
    }
}
