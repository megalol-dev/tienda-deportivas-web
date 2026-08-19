// Devuelve los datos públicos de un usuario.
package com.tiendadeportivas.backend.model;

import java.time.LocalDateTime;

public class UsuarioRespuesta {

    private Long id;
    private String nombre;
    private String email;
    private RolUsuario rol;
    private LocalDateTime fechaAlta;

    // Crea una instancia de UsuarioRespuesta.
    public UsuarioRespuesta() {
    }

    // Crea una instancia de UsuarioRespuesta.
    public UsuarioRespuesta(
            Long id,
            String nombre,
            String email,
            RolUsuario rol,
            LocalDateTime fechaAlta) {

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.fechaAlta = fechaAlta;
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Actualiza el identificador.
    public void setId(Long id) {
        this.id = id;
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

    // Devuelve el valor de rol.
    public RolUsuario getRol() {
        return rol;
    }

    // Actualiza el valor de rol.
    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    // Devuelve la fecha de alta.
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    // Actualiza la fecha de alta.
    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
}
