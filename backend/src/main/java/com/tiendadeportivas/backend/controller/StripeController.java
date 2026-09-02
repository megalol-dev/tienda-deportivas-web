// Crea sesiones de pago de Stripe.
package com.tiendadeportivas.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.tiendadeportivas.backend.service.StripeService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.transaction.annotation.Transactional;

import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.service.PedidoService;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

        private final StripeService stripeService;
        private final PedidoService pedidoService;

        // Crea una instancia de StripeController.
        public StripeController(
                        StripeService stripeService,
                        PedidoService pedidoService) {

                this.stripeService = stripeService;
                this.pedidoService = pedidoService;
        }

        // Crea un Checkout para un pedido guardado.
        @PostMapping("/checkout/pedido")
        @Transactional
        public ResponseEntity<?> crearCheckoutPedido(
                        @RequestParam String idPedido) {

                try {

                        Authentication authentication = SecurityContextHolder
                                        .getContext()
                                        .getAuthentication();

                        if (authentication == null
                                        || !authentication.isAuthenticated()
                                        || "anonymousUser".equals(authentication.getPrincipal())) {

                                return ResponseEntity
                                                .status(401)
                                                .body(Map.of(
                                                                "error",
                                                                "Debes iniciar sesión para realizar el pago."));
                        }

                        String emailUsuario = authentication.getName();

                        Pedido pedido = pedidoService.obtenerPedidoPorIdPedido(
                                        idPedido,
                                        emailUsuario);

                        pedidoService.validarPedidoPuedeIniciarPago(pedido);

                        if (pedido.getStripeSessionId() != null
                                        && !pedido.getStripeSessionId().isBlank()) {

                                Session sessionExistente = stripeService.obtenerSesionCheckout(
                                                pedido.getStripeSessionId());

                                String estadoSesion = sessionExistente.getStatus();

                                if ("open".equals(estadoSesion)) {

                                        return ResponseEntity.ok(
                                                        Map.of(
                                                                        "url", sessionExistente.getUrl(),
                                                                        "stripeSessionId", sessionExistente.getId()));
                                }

                                if ("complete".equals(estadoSesion)) {

                                        throw new IllegalStateException(
                                                        "El pago de este pedido ya ha sido completado en Stripe y está pendiente de confirmación.");
                                }

                                if (!"expired".equals(estadoSesion)) {

                                        throw new IllegalStateException(
                                                        "El estado de la sesión de Stripe no permite iniciar un nuevo pago.");
                                }
                        }

                        String idempotencyKey;

                        if (pedido.getStripeSessionId() == null
                                        || pedido.getStripeSessionId().isBlank()) {

                                idempotencyKey = "checkout-pedido-"
                                                + pedido.getIdPedido()
                                                + "-inicial";

                        } else {

                                idempotencyKey = "checkout-pedido-"
                                                + pedido.getIdPedido()
                                                + "-reintento-"
                                                + pedido.getStripeSessionId();
                        }

                        Session session = stripeService.crearSesionCheckoutPedido(
                                        pedido,
                                        idempotencyKey);

                        pedidoService.guardarStripeSessionId(
                                        pedido,
                                        session.getId());

                        return ResponseEntity.ok(
                                        Map.of(
                                                        "url", session.getUrl(),
                                                        "stripeSessionId", session.getId()));

                } catch (IllegalArgumentException | IllegalStateException e) {

                        return ResponseEntity
                                        .badRequest()
                                        .body(Map.of(
                                                        "error",
                                                        e.getMessage()));

                } catch (SecurityException e) {

                        return ResponseEntity
                                        .status(403)
                                        .body(Map.of(
                                                        "error",
                                                        e.getMessage()));

                } catch (StripeException e) {

                        return ResponseEntity
                                        .internalServerError()
                                        .body(Map.of(
                                                        "error",
                                                        "No se pudo crear la sesión de pago."));
                }
        }
}
