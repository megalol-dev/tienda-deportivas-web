// Representa el carrito persistente de un usuario.
package com.tiendadeportivas.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

    // Crea una instancia de Carrito.
    public Carrito() {
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el valor de usuario.
    public Usuario getUsuario() {
        return usuario;
    }

    // Actualiza el valor de usuario.
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Devuelve el valor de items.
    public List<CarritoItem> getItems() {
        return items;
    }

    // Actualiza el valor de items.
    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }

    // Añade una línea y enlaza su propietario.
    public void agregarItem(CarritoItem item) {
        items.add(item);
        item.setCarrito(this);
    }

    // Elimina una línea del carrito.
    public void eliminarItem(CarritoItem item) {
        items.remove(item);
        item.setCarrito(null);
    }

    // Elimina todas las líneas del carrito.
    public void vaciar() {
        items.clear();
    }
}
