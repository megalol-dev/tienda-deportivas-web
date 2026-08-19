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

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tiendadeportivas.backend.model.CarritoItem;
import com.tiendadeportivas.backend.service.CarritoService;
import org.springframework.transaction.annotation.Transactional;

import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.service.PedidoService;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeService stripeService;
    private final CarritoService carritoService;
    private final PedidoService pedidoService;

    // Crea una instancia de StripeController.
    public StripeController(
            StripeService stripeService,
            CarritoService carritoService,
            PedidoService pedidoService) {

        this.stripeService = stripeService;
        this.carritoService = carritoService;
        this.pedidoService = pedidoService;
    }

    // Crea un Checkout para un producto.
    @PostMapping("/checkout")
    public ResponseEntity<?> crearCheckout(
            @RequestParam String nombre,
            @RequestParam long precio,
            @RequestParam long cantidad) {

        try {

            Session session = stripeService.crearSesionCheckout(
                    nombre,
                    precio,
                    cantidad);

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

    // Crea un Checkout con el carrito autenticado.
    @PostMapping("/checkout/carrito")
    @Transactional
    public ResponseEntity<?> crearCheckoutCarrito() {

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

            List<CarritoItem> carrito = carritoService.obtenerItemsEntidad(emailUsuario);

            if (carrito.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(Map.of(
                                "error",
                                "No se puede pagar un carrito vacío."));
            }

            Session session = stripeService.crearSesionCheckoutCarrito(carrito);

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

            Session session = stripeService.crearSesionCheckoutPedido(pedido);

            pedidoService.guardarStripeSessionId(
                    pedido,
                    session.getId());

            return ResponseEntity.ok(
                    Map.of(
                            "url", session.getUrl(),
                            "stripeSessionId", session.getId()));

        } catch (IllegalArgumentException e) {

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
