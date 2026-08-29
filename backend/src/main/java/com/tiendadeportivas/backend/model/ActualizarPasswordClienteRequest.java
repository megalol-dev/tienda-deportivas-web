// Transporta la contraseña actual y la nueva contraseña del cliente.
package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ActualizarPasswordClienteRequest {

    @NotBlank(message = "La contraseña actual es obligatoria.")
    @Size(max = 72, message = "La contraseña actual no puede superar los 72 caracteres.")
    private String passwordActual;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres.")
    @Pattern(regexp = "^(?=.*\\p{L})(?=.*\\d).+$", message = "La contraseña debe contener al menos una letra y un número.")
    private String password;

    @NotBlank(message = "Debes confirmar la contraseña.")
    @Size(min = 8, max = 72, message = "La confirmación de contraseña debe tener entre 8 y 72 caracteres.")
    private String confirmarPassword;

    // Crea una instancia de ActualizarPasswordClienteRequest.
    public ActualizarPasswordClienteRequest() {
    }

    // Devuelve el valor de password.
    public String getPassword() {
        return password;
    }

    // Devuelve la confirmación de contraseña.
    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    // Actualiza el valor de password.
    public void setPassword(String password) {
        this.password = password;
    }

    // Actualiza la confirmación de contraseña.
    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    // Devuelve la contraseña actual del cliente.
    public String getPasswordActual() {
        return passwordActual;
    }

    // Actualiza la contraseña actual del cliente.
    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }
}
