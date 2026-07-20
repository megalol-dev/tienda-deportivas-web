package com.tiendadeportivas.backend.model;

public class CarritoItem {

    private int idProducto;
    private int talla;
    private String color;
    private int cantidad;

    public CarritoItem() {
    }

    public CarritoItem(int idProducto, int talla, String color, int cantidad) {
        this.idProducto = idProducto;
        this.talla = talla;
        this.color = color;
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getTalla() {
        return talla;
    }

    public void setTalla(int talla) {
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
}
