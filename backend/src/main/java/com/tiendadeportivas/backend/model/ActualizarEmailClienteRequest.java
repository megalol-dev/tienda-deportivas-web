package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// =====================================================
// DTO PARA ACTUALIZAR EL EMAIL DEL CLIENTE
// -----------------------------------------------------
// Se utiliza desde el área personal del cliente.
//
// Mantiene las mismas reglas de email utilizadas
// durante el registro de usuarios y empleados.
// =====================================================

public class ActualizarEmailClienteRequest {

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres.")
    private String email;

    // =====================================================
    // CONSTRUCTOR VACÍO
    // =====================================================

    public ActualizarEmailClienteRequest() {
    }

    // =====================================================
    // GETTER
    // =====================================================

    public String getEmail() {
        return email;
    }

    // =====================================================
    // SETTER
    // =====================================================

    public void setEmail(String email) {
        this.email = email;
    }
}