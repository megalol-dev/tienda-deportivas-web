// Procesa y verifica los eventos enviados por Stripe.
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

    // Crea una instancia de StripeWebhookController.
    public StripeWebhookController(
            PedidoService pedidoService) {

        this.pedidoService = pedidoService;
    }

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    // Verifica y procesa un evento de Stripe.
    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {

        try {

            Event event = Webhook.constructEvent(
                    payload,
                    signature,
                    webhookSecret);

            System.out.println(
                    "Evento recibido desde Stripe: "
                            + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {

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

                String idPedido = session
                        .getMetadata()
                        .get("idPedido");

                String stripeSessionId = session.getId();

                pedidoService.confirmarPagoStripe(
                        stripeSessionId,
                        idPedido);

                System.out.println(
                        "Pago confirmado correctamente: "
                                + idPedido);
            }

            return ResponseEntity.ok("Webhook recibido.");

        } catch (SignatureVerificationException e) {

            System.err.println(
                    "Firma del webhook de Stripe no válida.");

            return ResponseEntity
                    .badRequest()
                    .body("Firma de Stripe no válida.");
        }
    }
}
