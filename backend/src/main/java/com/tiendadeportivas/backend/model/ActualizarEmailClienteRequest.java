// Transporta el nuevo email del cliente.
package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActualizarEmailClienteRequest {

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres.")
    private String email;

    // Crea una instancia de ActualizarEmailClienteRequest.
    public ActualizarEmailClienteRequest() {
    }

    // Devuelve el valor de email.
    public String getEmail() {
        return email;
    }

    // Actualiza el valor de email.
    public void setEmail(String email) {
        this.email = email;
    }
}
