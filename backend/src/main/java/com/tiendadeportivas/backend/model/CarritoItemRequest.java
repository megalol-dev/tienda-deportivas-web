// Transporta un producto que se añade al carrito.
package com.tiendadeportivas.backend.model;

public class CarritoItemRequest {

    private int idProducto;
    private int talla;
    private String color;
    private int cantidad;

    // Crea una instancia de CarritoItemRequest.
    public CarritoItemRequest() {
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
