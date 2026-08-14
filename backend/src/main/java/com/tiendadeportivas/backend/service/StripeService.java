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

    // =====================================================
    // CREAR CHECKOUT DESDE EL CARRITO REAL
    // -----------------------------------------------------
    // Los precios NO llegan desde el frontend.
    //
    // Se obtienen directamente de los productos guardados
    // en MariaDB para evitar manipulaciones del precio.
    // =====================================================

    public Session crearSesionCheckoutCarrito(
            List<CarritoItem> carrito) throws StripeException {

        // =================================================
        // VALIDAR CARRITO
        // =================================================

        if (carrito == null || carrito.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede iniciar un pago con el carrito vacío.");
        }

        // =================================================
        // CALCULAR SUBTOTAL REAL DEL CARRITO
        // -------------------------------------------------
        // Los precios proceden siempre de MariaDB.
        // Nunca confiamos en precios enviados por frontend.
        // =================================================

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

        // =================================================
        // CALCULAR IVA
        // -------------------------------------------------
        // Aplicamos exactamente la misma regla que utiliza
        // PedidoService: 21 % sobre el subtotal.
        // =================================================

        BigDecimal iva = subtotal
                .multiply(BigDecimal.valueOf(0.21))
                .setScale(2, RoundingMode.HALF_UP);

        // =================================================
        // CALCULAR GASTOS DE ENVÍO
        // -------------------------------------------------
        // Pedidos de 100 € o más:
        // envío GRATIS.
        //
        // Pedidos inferiores a 100 €:
        // envío = 4,99 €.
        // =================================================

        BigDecimal envio;

        if (subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {

            envio = new BigDecimal("0.00");

        } else {

            envio = new BigDecimal("4.99");
        }

        // =================================================
        // CREAR LA SESIÓN DE STRIPE
        // =================================================

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(
                                        "http://localhost:5500/")
                        .setCancelUrl(
                                        "http://localhost:5500/");

        // =================================================
        // CONVERTIR CADA ITEM DEL CARRITO EN UNA LÍNEA STRIPE
        // =================================================

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

            // =============================================
            // CONVERTIR EUROS A CÉNTIMOS
            //
            // Ejemplo:
            // 109.99 € -> 10999
            // =============================================

            long precioEnCentimos = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValueExact();

            // =============================================
            // DATOS DEL PRODUCTO PARA STRIPE
            // =============================================

            SessionCreateParams.LineItem.PriceData.ProductData productoStripe = SessionCreateParams.LineItem.PriceData.ProductData
                    .builder()
                    .setName(
                            producto.getMarca()
                                    + " "
                                    + producto.getNombre())
                    .build();

            // =============================================
            // PRECIO REAL OBTENIDO DESDE MARIADB
            // =============================================

            SessionCreateParams.LineItem.PriceData precioStripe = SessionCreateParams.LineItem.PriceData
                    .builder()
                    .setCurrency("eur")
                    .setUnitAmount(precioEnCentimos)
                    .setProductData(productoStripe)
                    .build();

            // =============================================
            // LÍNEA DEL CHECKOUT
            // =============================================

            SessionCreateParams.LineItem linea = SessionCreateParams.LineItem
                    .builder()
                    .setQuantity((long) item.getCantidad())
                    .setPriceData(precioStripe)
                    .build();

            paramsBuilder.addLineItem(linea);
        }

        // =================================================
        // AÑADIR IVA AL CHECKOUT
        // =================================================

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

        // =================================================
        // AÑADIR GASTOS DE ENVÍO
        // -------------------------------------------------
        // Solo añadimos esta línea cuando realmente
        // existen gastos de envío.
        // =================================================

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

        // =================================================
        // CREAR SESIÓN EN STRIPE
        // =================================================

        return Session.create(paramsBuilder.build());
    }


    // =====================================================
    // CREAR CHECKOUT DESDE UN PEDIDO YA GUARDADO
    // -----------------------------------------------------
    // Stripe ya no depende del carrito.
    //
    // Utilizamos los datos guardados en Pedido y PedidoItem.
    // De esta forma el pedido queda congelado antes de pagar.
    // =====================================================

    public Session crearSesionCheckoutPedido(
            Pedido pedido) throws StripeException {

        // =================================================
        // VALIDAR PEDIDO
        // =================================================

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido no puede ser nulo.");
        }

        if (pedido.getItems() == null
                || pedido.getItems().isEmpty()) {

            throw new IllegalArgumentException(
                    "No se puede pagar un pedido sin productos.");
        }

        // =================================================
        // CREAR CONFIGURACIÓN DE STRIPE
        // =================================================

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)

                        // Relacionamos la sesión de Stripe
                        // con nuestro pedido de MariaDB.
                        .putMetadata(
                                        "idPedido",
                                        pedido.getIdPedido())

                        // Pago realizado correctamente.
                        // Volvemos a nuestra tienda.
                        .setSuccessUrl(
                                        "http://localhost:5500/frontend/tienda.html?pago=exito&idPedido="
                                                        + pedido.getIdPedido())

                        // Si el usuario cancela el pago,
                        // también volvemos a nuestra tienda.
                        .setCancelUrl(
                                        "http://localhost:5500/frontend/tienda.html?pago=cancelado&idPedido="
                                                        + pedido.getIdPedido());

        // =================================================
        // PRODUCTOS DEL PEDIDO
        // =================================================

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

        // =================================================
        // IVA DEL PEDIDO
        // -------------------------------------------------
        // Ya NO volvemos a calcular el 21 %.
        // Utilizamos exactamente el IVA que quedó guardado
        // en el pedido.
        // =================================================

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

        // =================================================
        // GASTOS DE ENVÍO DEL PEDIDO
        // -------------------------------------------------
        // Igual que con el IVA: utilizamos el valor que ya
        // quedó calculado y guardado en PedidoService.
        // =================================================

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

        // =================================================
        // CREAR SESIÓN EN STRIPE
        // =================================================

        return Session.create(paramsBuilder.build());
    }
}
