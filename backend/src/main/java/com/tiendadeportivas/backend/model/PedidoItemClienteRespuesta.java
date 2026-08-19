// Devuelve una línea de pedido al cliente.
package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;

public class PedidoItemClienteRespuesta {

    private int productoId;
    private String nombreProducto;
    private String talla;
    private String color;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotalLinea;

    // Crea una instancia de PedidoItemClienteRespuesta.
    public PedidoItemClienteRespuesta() {
    }

    // Crea una instancia de PedidoItemClienteRespuesta.
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

    // Devuelve el identificador del producto.
    public int getProductoId() {
        return productoId;
    }

    // Actualiza el identificador del producto.
    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    // Devuelve el nombre del producto.
    public String getNombreProducto() {
        return nombreProducto;
    }

    // Actualiza el nombre del producto.
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // Devuelve el valor de talla.
    public String getTalla() {
        return talla;
    }

    // Actualiza el valor de talla.
    public void setTalla(String talla) {
        this.talla = talla;
    }

    // Devuelve el valor de color.
    public String getColor() {
        return color;
    }

    // Actualiza el valor de color.
    public void setColor(String color) {
        this.color = color;
    }

    // Devuelve el valor de cantidad.
    public int getCantidad() {
        return cantidad;
    }

    // Actualiza el valor de cantidad.
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Devuelve el precio unitario.
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    // Actualiza el precio unitario.
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    // Devuelve el subtotal de la línea.
    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }

    // Actualiza el subtotal de la línea.
    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }
}
