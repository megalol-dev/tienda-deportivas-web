// Representa una línea del carrito.
package com.tiendadeportivas.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "carrito_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "carrito_id",
                "producto_id",
                "talla",
                "color"
        })
})
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private int talla;

    private String color;

    private int cantidad;

    // Crea una instancia de CarritoItem.
    public CarritoItem() {
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el valor de carrito.
    public Carrito getCarrito() {
        return carrito;
    }

    // Actualiza el valor de carrito.
    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    // Devuelve el valor de producto.
    public Producto getProducto() {
        return producto;
    }

    // Actualiza el valor de producto.
    public void setProducto(Producto producto) {
        this.producto = producto;
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
