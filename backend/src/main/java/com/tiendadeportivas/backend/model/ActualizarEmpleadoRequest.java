package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// =====================================================
// DTO PARA ACTUALIZAR UN EMPLEADO
// -----------------------------------------------------
// Permite modificar los datos básicos de un empleado.
//
// La contraseña es opcional:
//
// - Si queda vacía, se conserva la contraseña actual.
// - Si se introduce una nueva contraseña, deberá
//   cumplir las reglas de seguridad establecidas.
// =====================================================

public class ActualizarEmpleadoRequest {

    // =====================================================
    // NOMBRE
    // =====================================================

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "El nombre contiene caracteres no válidos.")
    private String nombre;

    // =====================================================
    // EMAIL
    // =====================================================

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres.")
    private String email;

    // =====================================================
    // NUEVA CONTRASEÑA
    // -----------------------------------------------------
    // Campo opcional.
    //
    // "" -> conserva la contraseña actual.
    //
    // Si se introduce una nueva:
    // - Entre 8 y 72 caracteres.
    // - Al menos una letra.
    // - Al menos un número.
    // =====================================================

    @Pattern(regexp = "^$|^(?=.*\\p{L})(?=.*\\d).{8,72}$", message = "La contraseña debe tener entre 8 y 72 caracteres y contener al menos una letra y un número.")
    private String password;

    // =====================================================
    // CONFIRMAR NUEVA CONTRASEÑA
    // -----------------------------------------------------
    // La comparación entre ambas contraseñas se realizará
    // en UsuarioService.
    // =====================================================

    private String confirmarPassword;

    // =====================================================
    // ROL
    // =====================================================

    @NotNull(message = "El rol es obligatorio.")
    private RolUsuario rol;

    // =====================================================
    // ESTADO
    // =====================================================

    private boolean activo;

    // =====================================================
    // CONSTRUCTOR VACÍO
    // =====================================================

    public ActualizarEmpleadoRequest() {
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
