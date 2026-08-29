// Representa un usuario persistente.
package com.tiendadeportivas.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import com.tiendadeportivas.backend.model.ActualizarEmailPersonalRequest;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RolUsuario rol;

    @Column(nullable = false)
    private LocalDateTime fechaAlta;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidos = new ArrayList<>();

    @OneToOne(
        mappedBy = "usuario",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private Carrito carrito;

    // Crea una instancia de Usuario.
    public Usuario() {
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
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

    // Indica si el registro está activo.
    public boolean isActivo() {
        return activo;
    }

    // Actualiza el valor de activo.
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Devuelve el valor de pedidos.
    public List<Pedido> getPedidos() {
        return pedidos;
    }

    // Actualiza el valor de pedidos.
    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    // Devuelve el valor de carrito.
    public Carrito getCarrito() {
        return carrito;
    }

    // Actualiza el valor de carrito.
    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }
}
