// Transporta los datos editables de un producto.
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

    // Crea una instancia de ProductoRequest.
    public ProductoRequest() {
    }

    // Devuelve el valor de marca.
    public String getMarca() {
        return marca;
    }

    // Actualiza el valor de marca.
    public void setMarca(String marca) {
        this.marca = marca;
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Actualiza el valor de nombre.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve el valor de precio.
    public BigDecimal getPrecio() {
        return precio;
    }

    // Actualiza el valor de precio.
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    // Devuelve el valor de tallas.
    public List<Integer> getTallas() {
        return tallas;
    }

    // Actualiza el valor de tallas.
    public void setTallas(List<Integer> tallas) {
        this.tallas = tallas;
    }

    // Devuelve el valor de colores.
    public List<String> getColores() {
        return colores;
    }

    // Actualiza el valor de colores.
    public void setColores(List<String> colores) {
        this.colores = colores;
    }

    // Indica si el registro está activo.
    public boolean isActivo() {
        return activo;
    }

    // Actualiza el valor de activo.
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
