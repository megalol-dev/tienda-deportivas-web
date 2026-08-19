// Representa un producto del catálogo.
package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean activo = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "producto_tallas", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "talla", nullable = false)
    @OrderColumn(name = "orden_talla")
    private List<Integer> tallas = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "producto_colores", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "color", nullable = false, length = 50)
    @OrderColumn(name = "orden_color")
    private List<String> colores = new ArrayList<>();

    // Crea una instancia de Producto.
    public Producto() {
    }

    // Crea una instancia de Producto.
    public Producto(
            Long id,
            String marca,
            String nombre,
            BigDecimal precio,
            List<Integer> tallas,
            List<String> colores) {

        this.id = id;
        this.marca = marca;
        this.nombre = nombre;
        this.precio = precio;
        this.tallas = tallas;
        this.colores = colores;
        this.activo = true;
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el valor de marca.
    public String getMarca() {
        return marca;
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Devuelve el valor de precio.
    public BigDecimal getPrecio() {
        return precio;
    }

    // Indica si el registro está activo.
    public boolean isActivo() {
        return activo;
    }

    // Devuelve el valor de tallas.
    public List<Integer> getTallas() {
        return tallas;
    }

    // Devuelve el valor de colores.
    public List<String> getColores() {
        return colores;
    }

    // Actualiza el identificador.
    public void setId(Long id) {
        this.id = id;
    }

    // Actualiza el valor de marca.
    public void setMarca(String marca) {
        this.marca = marca;
    }

    // Actualiza el valor de nombre.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Actualiza el valor de precio.
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    // Actualiza el valor de activo.
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Actualiza el valor de tallas.
    public void setTallas(List<Integer> tallas) {
        this.tallas = tallas;
    }

    // Actualiza el valor de colores.
    public void setColores(List<String> colores) {
        this.colores = colores;
    }
}
