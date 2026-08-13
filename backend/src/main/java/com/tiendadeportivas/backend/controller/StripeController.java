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

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeService stripeService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    // =====================================================
    // CREAR SESIÓN DE CHECKOUT
    // =====================================================

    @PostMapping("/checkout")
    public ResponseEntity<?> crearCheckout(
            @RequestParam String nombre,
            @RequestParam long precio,
            @RequestParam long cantidad) {

        try {

            // Pedimos al servicio que cree una sesión en Stripe
            Session session = stripeService.crearSesionCheckout(
                    nombre,
                    precio,
                    cantidad);

            // Stripe nos devuelve una URL de pago.
            // Se la devolvemos al frontend.
            return ResponseEntity.ok(
                    Map.of("url", session.getUrl()));

        } catch (StripeException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(Map.of(
                            "error",
                            "No se pudo crear la sesión de pago."));
        }
    }
}
