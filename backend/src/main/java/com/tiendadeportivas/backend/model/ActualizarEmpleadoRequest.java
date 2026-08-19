// Transporta los cambios de un empleado.
package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ActualizarEmpleadoRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "El nombre contiene caracteres no válidos.")
    private String nombre;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres.")
    private String email;

    @Pattern(regexp = "^$|^(?=.*\\p{L})(?=.*\\d).{8,72}$", message = "La contraseña debe tener entre 8 y 72 caracteres y contener al menos una letra y un número.")
    private String password;

    private String confirmarPassword;

    @NotNull(message = "El rol es obligatorio.")
    private RolUsuario rol;

    private boolean activo;

    // Crea una instancia de ActualizarEmpleadoRequest.
    public ActualizarEmpleadoRequest() {
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Devuelve el valor de email.
    public String getEmail() {
        return email;
    }

    // Devuelve el valor de password.
    public String getPassword() {
        return password;
    }

    // Devuelve la confirmación de contraseña.
    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    // Devuelve el valor de rol.
    public RolUsuario getRol() {
        return rol;
    }

    // Indica si el registro está activo.
    public boolean isActivo() {
        return activo;
    }

    // Actualiza el valor de nombre.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Actualiza el valor de email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Actualiza el valor de password.
    public void setPassword(String password) {
        this.password = password;
    }

    // Actualiza la confirmación de contraseña.
    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    // Actualiza el valor de rol.
    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    // Actualiza el valor de activo.
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
