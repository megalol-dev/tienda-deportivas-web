package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductoRequest {

    @NotBlank(message = "La marca es obligatoria.")
    @Size(min = 2, max = 100, message = "La marca debe tener entre 2 y 100 caracteres.")
    private String marca;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres.")
    private String nombre;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser superior a 0.")
    @DecimalMax(value = "99999999.99", message = "El precio es demasiado alto.")
    private BigDecimal precio;

    @NotEmpty(message = "Debes introducir al menos una talla.")
    private List<Integer> tallas;

    @NotEmpty(message = "Debes introducir al menos un color.")
    private List<String> colores;

    private boolean activo;

    public ProductoRequest() {
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public List<Integer> getTallas() {
        return tallas;
    }

    public void setTallas(List<Integer> tallas) {
        this.tallas = tallas;
    }

    public List<String> getColores() {
        return colores;
    }

    public void setColores(List<String> colores) {
        this.colores = colores;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
