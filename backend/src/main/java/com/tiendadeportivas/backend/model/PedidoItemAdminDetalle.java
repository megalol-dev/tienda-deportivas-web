// Representa una línea de pedido visible desde el panel administrativo.
package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;

public class PedidoItemAdminDetalle {

    private int productoId;
    private String nombreProducto;
    private String talla;
    private String color;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotalLinea;

    public PedidoItemAdminDetalle(
            int productoId,
            String nombreProducto,
            String talla,
            String color,
            int cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotalLinea) {

        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.talla = talla;
        this.color = color;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotalLinea = subtotalLinea;
    }

    public int getProductoId() {
        return productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getTalla() {
        return talla;
    }

    public String getColor() {
        return color;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }
}
