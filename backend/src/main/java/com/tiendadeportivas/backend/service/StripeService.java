// Construye las sesiones de Checkout de Stripe.
package com.tiendadeportivas.backend.service;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.tiendadeportivas.backend.model.CarritoItem;
import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.model.PedidoItem;

@Service
public class StripeService {

    // Construye una sesión para un producto.
    public Session crearSesionCheckout(
            String nombreProducto,
            long precioEnCentimos,
            long cantidad) throws StripeException {

        SessionCreateParams.LineItem.PriceData.ProductData producto = SessionCreateParams.LineItem.PriceData.ProductData
                .builder()
                .setName(nombreProducto)
                .build();

        SessionCreateParams.LineItem.PriceData precio = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("eur")
                .setUnitAmount(precioEnCentimos)
                .setProductData(producto)
                .build();

        SessionCreateParams.LineItem linea = SessionCreateParams.LineItem.builder()
                .setQuantity(cantidad)
                .setPriceData(precio)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5500/frontend/pago-exito.html")
                .setCancelUrl("http://localhost:5500/frontend/pago-cancelado.html")
                .addLineItem(linea)
                .build();

        return Session.create(params);
    }

    // Construye una sesión desde el carrito.
    public Session crearSesionCheckoutCarrito(
            List<CarritoItem> carrito) throws StripeException {

        if (carrito == null || carrito.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede iniciar un pago con el carrito vacío.");
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CarritoItem item : carrito) {

            if (item.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad de un producto debe ser mayor que 0.");
            }

            Producto producto = item.getProducto();

            if (producto == null) {
                throw new IllegalStateException(
                        "Uno de los productos del carrito no existe.");
            }

            subtotal = subtotal.add(
                    producto.getPrecio()
                            .multiply(
                                    BigDecimal.valueOf(
                                            item.getCantidad())));
        }

        BigDecimal iva = subtotal
                .multiply(BigDecimal.valueOf(0.21))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal envio;

        if (subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {

            envio = new BigDecimal("0.00");

        } else {

            envio = new BigDecimal("4.99");
        }

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(
                                        "http://localhost:5500/")
                        .setCancelUrl(
                                        "http://localhost:5500/");

        for (CarritoItem item : carrito) {

            if (item.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad de un producto debe ser mayor que 0.");
            }

            Producto producto = item.getProducto();

            if (producto == null) {
                throw new IllegalStateException(
                        "Uno de los productos del carrito no existe.");
            }

            long precioEnCentimos = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValueExact();

            SessionCreateParams.LineItem.PriceData.ProductData productoStripe = SessionCreateParams.LineItem.PriceData.ProductData
                    .builder()
                    .setName(
                            producto.getMarca()
                                    + " "
                                    + producto.getNombre())
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

        long ivaEnCentimos = iva
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();

        if (ivaEnCentimos > 0) {

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

            SessionCreateParams.LineItem ivaLinea = SessionCreateParams.LineItem
                    .builder()
                    .setQuantity(1L)
                    .setPriceData(ivaPrecio)
                    .build();

            paramsBuilder.addLineItem(ivaLinea);
        }

        if (envio.compareTo(BigDecimal.ZERO) > 0) {

            long envioEnCentimos = envio
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

            SessionCreateParams.LineItem envioLinea = SessionCreateParams.LineItem
                    .builder()
                    .setQuantity(1L)
                    .setPriceData(envioPrecio)
                    .build();

            paramsBuilder.addLineItem(envioLinea);
        }

        return Session.create(paramsBuilder.build());
    }

    // Construye una sesión desde un pedido.
    public Session crearSesionCheckoutPedido(
            Pedido pedido) throws StripeException {

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

        return Session.create(paramsBuilder.build());
    }
}
