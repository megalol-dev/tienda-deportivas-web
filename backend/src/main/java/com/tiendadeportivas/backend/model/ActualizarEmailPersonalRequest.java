package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Transporta el cambio de email del personal autenticado.
public class ActualizarEmailPersonalRequest {

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres.")
    private String email;

    @NotBlank(message = "La contraseña actual es obligatoria.")
    @Size(max = 72, message = "La contraseña actual no puede superar los 72 caracteres.")
    private String passwordActual;

    // Crea una instancia de ActualizarEmailPersonalRequest.
    public ActualizarEmailPersonalRequest() {
    }

    // Devuelve el nuevo email.
    public String getEmail() {
        return email;
    }

    // Actualiza el nuevo email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Devuelve la contraseña actual del miembro del personal.
    public String getPasswordActual() {
        return passwordActual;
    }

    // Actualiza la contraseña actual del miembro del personal.
    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }
}
