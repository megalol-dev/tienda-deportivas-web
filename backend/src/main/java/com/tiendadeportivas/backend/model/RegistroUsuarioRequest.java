// Transporta los datos de registro de un cliente.
package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class RegistroUsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "El nombre contiene caracteres no válidos.")
    private String nombre;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres.")
    @Pattern(regexp = "^(?=.*\\p{L})(?=.*\\d).+$", message = "La contraseña debe contener al menos una letra y un número.")
    private String password;

    @NotBlank(message = "Debes confirmar la contraseña.")
    @Size(min = 8, max = 72, message = "La confirmación de contraseña debe tener entre 8 y 72 caracteres.")
    private String confirmarPassword;

    // Crea una instancia de RegistroUsuarioRequest.
    public RegistroUsuarioRequest() {
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Actualiza el valor de nombre.
    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    // Devuelve la confirmación de contraseña.
    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    // Actualiza la confirmación de contraseña.
    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }
}
