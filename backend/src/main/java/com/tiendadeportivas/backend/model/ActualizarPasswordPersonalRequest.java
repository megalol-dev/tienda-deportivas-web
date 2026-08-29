package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Transporta el cambio de contraseña del personal autenticado.
public class ActualizarPasswordPersonalRequest {

    @NotBlank(message = "La contraseña actual es obligatoria.")
    @Size(max = 72, message = "La contraseña actual no puede superar los 72 caracteres.")
    private String passwordActual;

    @NotBlank(message = "La nueva contraseña es obligatoria.")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres.")
    @Pattern(regexp = "^(?=.*\\p{L})(?=.*\\d).+$", message = "La contraseña debe contener al menos una letra y un número.")
    private String password;

    @NotBlank(message = "Debes confirmar la contraseña.")
    @Size(min = 8, max = 72, message = "La confirmación de contraseña debe tener entre 8 y 72 caracteres.")
    private String confirmarPassword;

    // Devuelve la contraseña actual del miembro del personal.
    public String getPasswordActual() {
        return passwordActual;
    }

    // Actualiza la contraseña actual del miembro del personal.
    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    // Devuelve la nueva contraseña.
    public String getPassword() {
        return password;
    }

    // Actualiza la nueva contraseña.
    public void setPassword(String password) {
        this.password = password;
    }

    // Devuelve la confirmación de la nueva contraseña.
    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    // Actualiza la confirmación de la nueva contraseña.
    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }
}
