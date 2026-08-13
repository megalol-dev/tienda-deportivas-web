package com.tiendadeportivas.backend.model;

import java.time.LocalDateTime;

public class EmpleadoRespuesta {

    private Long id;
    private String nombre;
    private String email;
    private RolUsuario rol;
    private LocalDateTime fechaAlta;
    private boolean activo;

    public EmpleadoRespuesta() {
    }

    public EmpleadoRespuesta(
            Long id,
            String nombre,
            String email,
            RolUsuario rol,
            LocalDateTime fechaAlta,
            boolean activo) {

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.fechaAlta = fechaAlta;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public boolean isActivo() {
        return activo;
    }
}
