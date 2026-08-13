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

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tiendadeportivas.backend.model.CarritoItem;
import com.tiendadeportivas.backend.service.CarritoService;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeService stripeService;
    private final CarritoService carritoService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public StripeController(
            StripeService stripeService,
            CarritoService carritoService) {

        this.stripeService = stripeService;
        this.carritoService = carritoService;
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

    // =====================================================
    // CREAR CHECKOUT DESDE EL CARRITO DEL USUARIO
    // =====================================================

    @PostMapping("/checkout/carrito")
    @Transactional
    public ResponseEntity<?> crearCheckoutCarrito() {

        try {

            // =============================================
            // OBTENER USUARIO AUTENTICADO
            // =============================================

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

            // =============================================
            // OBTENER CARRITO REAL DESDE MARIADB
            // =============================================

            List<CarritoItem> carrito = carritoService.obtenerItemsEntidad(emailUsuario);

            if (carrito.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(Map.of(
                                "error",
                                "No se puede pagar un carrito vacío."));
            }

            // =============================================
            // CREAR CHECKOUT EN STRIPE
            // =============================================

            Session session = stripeService.crearSesionCheckoutCarrito(carrito);

            // =============================================
            // DEVOLVER URL AL FRONTEND
            // =============================================

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
