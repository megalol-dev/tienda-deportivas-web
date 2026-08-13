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

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // DATOS PRINCIPALES
    // =====================================================

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // =====================================================
    // ESTADO DEL PRODUCTO
    // -----------------------------------------------------
    // En lugar de borrar físicamente productos,
    // podremos desactivarlos.
    // =====================================================

    @Column(nullable = false)
    private boolean activo = true;

    // =====================================================
    // TALLAS
    // -----------------------------------------------------
    // JPA creará automáticamente:
    //
    // producto_tallas
    // ├── producto_id
    // ├── talla
    // └── orden_talla
    //
    // El orden se conserva gracias a @OrderColumn.
    // =====================================================

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "producto_tallas", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "talla", nullable = false)
    @OrderColumn(name = "orden_talla")
    private List<Integer> tallas = new ArrayList<>();

    // =====================================================
    // COLORES
    // -----------------------------------------------------
    // JPA creará automáticamente:
    //
    // producto_colores
    // ├── producto_id
    // ├── color
    // └── orden_color
    //
    // Mantener el orden es importante porque actualmente
    // el frontend utiliza el primer color como color inicial.
    // =====================================================

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "producto_colores", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "color", nullable = false, length = 50)
    @OrderColumn(name = "orden_color")
    private List<String> colores = new ArrayList<>();

    // =====================================================
    // CONSTRUCTOR VACÍO
    // =====================================================

    public Producto() {
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

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

    // =====================================================
    // GETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public List<Integer> getTallas() {
        return tallas;
    }

    public List<String> getColores() {
        return colores;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setId(Long id) {
        this.id = id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setTallas(List<Integer> tallas) {
        this.tallas = tallas;
    }

    public void setColores(List<String> colores) {
        this.colores = colores;
    }
}