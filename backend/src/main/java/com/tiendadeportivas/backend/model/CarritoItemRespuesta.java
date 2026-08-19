// Devuelve una línea simplificada del carrito.
package com.tiendadeportivas.backend.model;

public class CarritoItemRespuesta {

    private int idProducto;
    private int talla;
    private String color;
    private int cantidad;

    // Crea una instancia de CarritoItemRespuesta.
    public CarritoItemRespuesta() {
    }

    // Crea una instancia de CarritoItemRespuesta.
    public CarritoItemRespuesta(
            int idProducto,
            int talla,
            String color,
            int cantidad) {

        this.idProducto = idProducto;
        this.talla = talla;
        this.color = color;
        this.cantidad = cantidad;
    }

    // Devuelve el valor de id producto.
    public int getIdProducto() {
        return idProducto;
    }

    // Actualiza el valor de id producto.
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    // Devuelve el valor de talla.
    public int getTalla() {
        return talla;
    }

    // Actualiza el valor de talla.
    public void setTalla(int talla) {
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
}
