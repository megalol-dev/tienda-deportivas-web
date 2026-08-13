package com.tiendadeportivas.backend.service;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeService {

    public Session crearSesionCheckout(
            String nombreProducto,
            long precioEnCentimos,
            long cantidad) throws StripeException {

        // =====================================================
        // PRODUCTO QUE STRIPE MOSTRARÁ EN EL CHECKOUT
        // =====================================================

        SessionCreateParams.LineItem.PriceData.ProductData producto = SessionCreateParams.LineItem.PriceData.ProductData
                .builder()
                .setName(nombreProducto)
                .build();

        // =====================================================
        // PRECIO DEL PRODUCTO
        // =====================================================

        SessionCreateParams.LineItem.PriceData precio = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("eur")
                .setUnitAmount(precioEnCentimos)
                .setProductData(producto)
                .build();

        // =====================================================
        // LÍNEA DEL CARRITO
        // =====================================================

        SessionCreateParams.LineItem linea = SessionCreateParams.LineItem.builder()
                .setQuantity(cantidad)
                .setPriceData(precio)
                .build();

        // =====================================================
        // CONFIGURACIÓN DEL CHECKOUT
        // =====================================================

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5500/frontend/pago-exito.html")
                .setCancelUrl("http://localhost:5500/frontend/pago-cancelado.html")
                .addLineItem(linea)
                .build();

        // =====================================================
        // STRIPE CREA LA SESIÓN DE PAGO
        // =====================================================

        return Session.create(params);
    }
}
