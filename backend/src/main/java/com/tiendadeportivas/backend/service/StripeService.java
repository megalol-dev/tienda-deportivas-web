// Construye las sesiones de Checkout de Stripe.
package com.tiendadeportivas.backend.service;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.model.PedidoItem;

import com.stripe.net.RequestOptions;

@Service
public class StripeService {

        // Recupera una sesión de Checkout existente.
        public Session obtenerSesionCheckout(
                        String stripeSessionId) throws StripeException {

                if (stripeSessionId == null || stripeSessionId.isBlank()) {
                        throw new IllegalArgumentException(
                                        "El identificador de Stripe no puede estar vacío.");
                }

                return Session.retrieve(stripeSessionId);
        }

        // Construye una sesión desde un pedido.
        public Session crearSesionCheckoutPedido(Pedido pedido, String idempotencyKey)
                        throws StripeException {

                if (pedido == null) {
                        throw new IllegalArgumentException(
                                        "El pedido no puede ser nulo.");
                }

                if (pedido.getItems() == null
                                || pedido.getItems().isEmpty()) {

                        throw new IllegalArgumentException(
                                        "No se puede pagar un pedido sin productos.");
                }

                SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.PAYMENT)

                                .putMetadata(
                                                "idPedido",
                                                pedido.getIdPedido())

                                .setSuccessUrl(
                                                "http://localhost:5500/frontend/tienda.html?pago=exito&idPedido="
                                                                + pedido.getIdPedido())

                                .setCancelUrl(
                                                "http://localhost:5500/frontend/tienda.html?pago=cancelado&idPedido="
                                                                + pedido.getIdPedido());

                for (PedidoItem item : pedido.getItems()) {

                        if (item.getCantidad() <= 0) {
                                throw new IllegalArgumentException(
                                                "La cantidad de un producto debe ser mayor que 0.");
                        }

                        long precioEnCentimos = item.getPrecioUnitario()
                                        .multiply(BigDecimal.valueOf(100))
                                        .setScale(0, RoundingMode.HALF_UP)
                                        .longValueExact();

                        SessionCreateParams.LineItem.PriceData.ProductData productoStripe = SessionCreateParams.LineItem.PriceData.ProductData
                                        .builder()
                                        .setName(item.getNombreProducto())
                                        .build();

                        SessionCreateParams.LineItem.PriceData precioStripe = SessionCreateParams.LineItem.PriceData
                                        .builder()
                                        .setCurrency("eur")
                                        .setUnitAmount(precioEnCentimos)
                                        .setProductData(productoStripe)
                                        .build();

                        SessionCreateParams.LineItem linea = SessionCreateParams.LineItem
                                        .builder()
                                        .setQuantity((long) item.getCantidad())
                                        .setPriceData(precioStripe)
                                        .build();

                        paramsBuilder.addLineItem(linea);
                }

                if (pedido.getIva() != null
                                && pedido.getIva().compareTo(BigDecimal.ZERO) > 0) {

                        long ivaEnCentimos = pedido.getIva()
                                        .multiply(BigDecimal.valueOf(100))
                                        .setScale(0, RoundingMode.HALF_UP)
                                        .longValueExact();

                        SessionCreateParams.LineItem.PriceData.ProductData ivaProducto = SessionCreateParams.LineItem.PriceData.ProductData
                                        .builder()
                                        .setName("IVA (21%)")
                                        .build();

                        SessionCreateParams.LineItem.PriceData ivaPrecio = SessionCreateParams.LineItem.PriceData
                                        .builder()
                                        .setCurrency("eur")
                                        .setUnitAmount(ivaEnCentimos)
                                        .setProductData(ivaProducto)
                                        .build();

                        paramsBuilder.addLineItem(
                                        SessionCreateParams.LineItem
                                                        .builder()
                                                        .setQuantity(1L)
                                                        .setPriceData(ivaPrecio)
                                                        .build());
                }

                if (pedido.getEnvio() != null
                                && pedido.getEnvio().compareTo(BigDecimal.ZERO) > 0) {

                        long envioEnCentimos = pedido.getEnvio()
                                        .multiply(BigDecimal.valueOf(100))
                                        .setScale(0, RoundingMode.HALF_UP)
                                        .longValueExact();

                        SessionCreateParams.LineItem.PriceData.ProductData envioProducto = SessionCreateParams.LineItem.PriceData.ProductData
                                        .builder()
                                        .setName("Gastos de envío")
                                        .build();

                        SessionCreateParams.LineItem.PriceData envioPrecio = SessionCreateParams.LineItem.PriceData
                                        .builder()
                                        .setCurrency("eur")
                                        .setUnitAmount(envioEnCentimos)
                                        .setProductData(envioProducto)
                                        .build();

                        paramsBuilder.addLineItem(
                                        SessionCreateParams.LineItem
                                                        .builder()
                                                        .setQuantity(1L)
                                                        .setPriceData(envioPrecio)
                                                        .build());
                }

                RequestOptions requestOptions = RequestOptions.builder()
                                .setIdempotencyKey(idempotencyKey)
                                .build();

                return Session.create(
                                paramsBuilder.build(),
                                requestOptions);
        }
}
