// Transporta las credenciales de acceso.
package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Crea una instancia de LoginRequest.
    public LoginRequest() {
    }

    // Devuelve el valor de email.
    public String getEmail() {
        return email;
    }

    // Actualiza el valor de email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Devuelve el valor de password.
    public String getPassword() {
        return password;
    }

    // Actualiza el valor de password.
    public void setPassword(String password) {
        this.password = password;
    }
}
