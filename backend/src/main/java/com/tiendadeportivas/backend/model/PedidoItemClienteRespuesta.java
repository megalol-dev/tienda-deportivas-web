package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;

// =====================================================
// DTO DE PRODUCTO DENTRO DE UN PEDIDO DEL CLIENTE
// -----------------------------------------------------
// Representa únicamente los datos necesarios para
// mostrar el histórico del pedido en "Mis pedidos".
// =====================================================

public class PedidoItemClienteRespuesta {

    private int productoId;
    private String nombreProducto;
    private String talla;
    private String color;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotalLinea;

    public PedidoItemClienteRespuesta() {
    }

    public PedidoItemClienteRespuesta(
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

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }

    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }
}