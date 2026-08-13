package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// =====================================================
// DTO PARA ACTUALIZAR EL NOMBRE DEL CLIENTE
// -----------------------------------------------------
// Se utiliza desde el área personal del cliente.
//
// Las reglas son las mismas utilizadas durante
// el registro de usuarios y empleados.
// =====================================================

public class ActualizarNombreClienteRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "El nombre contiene caracteres no válidos.")
    private String nombre;

    // =====================================================
    // CONSTRUCTOR VACÍO
    // =====================================================

    public ActualizarNombreClienteRequest() {
    }

    // =====================================================
    // GETTER
    // =====================================================

    public String getNombre() {
        return nombre;
    }

    // =====================================================
    // SETTER
    // =====================================================

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}