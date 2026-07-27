package com.tiendadeportivas.backend.model;

import java.time.LocalDateTime;

public class UsuarioRespuesta {

    private Long id;
    private String nombre;
    private String email;
    private RolUsuario rol;
    private LocalDateTime fechaAlta;

    public UsuarioRespuesta() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
}
