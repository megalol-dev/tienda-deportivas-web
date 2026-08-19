// Devuelve los datos públicos de un empleado.
package com.tiendadeportivas.backend.model;

import java.time.LocalDateTime;

public class EmpleadoRespuesta {

    private Long id;
    private String nombre;
    private String email;
    private RolUsuario rol;
    private LocalDateTime fechaAlta;
    private boolean activo;

    // Crea una instancia de EmpleadoRespuesta.
    public EmpleadoRespuesta() {
    }

    // Crea una instancia de EmpleadoRespuesta.
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

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Devuelve el valor de email.
    public String getEmail() {
        return email;
    }

    // Devuelve el valor de rol.
    public RolUsuario getRol() {
        return rol;
    }

    // Devuelve la fecha de alta.
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    // Indica si el registro está activo.
    public boolean isActivo() {
        return activo;
    }
}
