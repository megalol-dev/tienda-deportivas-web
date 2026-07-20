package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import java.util.List;

public class Producto {

    private Long id;
    private String marca;
    private String nombre;
    private BigDecimal precio;
    private List<Integer> tallas;
    private List<String> colores;

    public Producto() {
    }

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
    }

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

    public List<Integer> getTallas() {
        return tallas;
    }

    public List<String> getColores() {
        return colores;
    }

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

    public void setTallas(List<Integer> tallas) {
        this.tallas = tallas;
    }

    public void setColores(List<String> colores) {
        this.colores = colores;
    }
}