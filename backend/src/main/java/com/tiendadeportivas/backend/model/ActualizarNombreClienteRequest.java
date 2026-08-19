// Transporta el nuevo nombre del cliente.
package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ActualizarNombreClienteRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "El nombre contiene caracteres no válidos.")
    private String nombre;

    // Crea una instancia de ActualizarNombreClienteRequest.
    public ActualizarNombreClienteRequest() {
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Actualiza el valor de nombre.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
