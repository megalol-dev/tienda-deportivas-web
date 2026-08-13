package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CrearEmpleadoRequest {

    // =====================================================
    // NOMBRE
    // -----------------------------------------------------
    // Permitimos letras, espacios, puntos, apóstrofes
    // y guiones.
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
    // CONTRASEÑA
    // -----------------------------------------------------
    // Permitimos símbolos.
    // Exigimos al menos una letra y un número.
    // =====================================================

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres.")
    @Pattern(regexp = "^(?=.*\\p{L})(?=.*\\d).+$", message = "La contraseña debe contener al menos una letra y un número.")
    private String password;

    // =====================================================
    // CONFIRMAR CONTRASEÑA
    // =====================================================

    @NotBlank(message = "Debes confirmar la contraseña.")
    @Size(min = 8, max = 72, message = "La confirmación de contraseña debe tener entre 8 y 72 caracteres.")
    private String confirmarPassword;

    // =====================================================
    // ROL
    // =====================================================

    @NotNull(message = "El rol es obligatorio.")
    private RolUsuario rol;

    private boolean activo;

    public CrearEmpleadoRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}