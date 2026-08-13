package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// =====================================================
// DTO PARA ACTUALIZAR LA CONTRASEÑA DEL CLIENTE
// -----------------------------------------------------
// Se utiliza desde el área personal del cliente.
//
// Mantiene las mismas reglas de contraseña utilizadas
// durante el registro de usuarios y empleados.
// =====================================================

public class ActualizarPasswordClienteRequest {

    // =====================================================
    // NUEVA CONTRASEÑA
    // -----------------------------------------------------
    // - Obligatoria.
    // - Entre 8 y 72 caracteres.
    // - Al menos una letra.
    // - Al menos un número.
    // =====================================================

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres.")
    @Pattern(regexp = "^(?=.*\\p{L})(?=.*\\d).+$", message = "La contraseña debe contener al menos una letra y un número.")
    private String password;

    // =====================================================
    // CONFIRMAR CONTRASEÑA
    // -----------------------------------------------------
    // La comparación entre ambas contraseñas se realiza
    // en UsuarioService.
    // =====================================================

    @NotBlank(message = "Debes confirmar la contraseña.")
    @Size(min = 8, max = 72, message = "La confirmación de contraseña debe tener entre 8 y 72 caracteres.")
    private String confirmarPassword;

    // =====================================================
    // CONSTRUCTOR VACÍO
    // =====================================================

    public ActualizarPasswordClienteRequest() {
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getPassword() {
        return password;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }
}
