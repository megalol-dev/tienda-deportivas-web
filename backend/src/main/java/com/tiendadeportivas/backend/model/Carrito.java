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

    // =====================================================
    // USUARIO PROPIETARIO
    // -----------------------------------------------------
    // Cada usuario tiene un único carrito.
    // =====================================================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    // =====================================================
    // ITEMS
    // -----------------------------------------------------
    // Si eliminamos un item del carrito, también se
    // elimina físicamente de carrito_items.
    // =====================================================

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

    public Carrito() {
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<CarritoItem> getItems() {
        return items;
    }

    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }

    public void agregarItem(CarritoItem item) {
        items.add(item);
        item.setCarrito(this);
    }

    public void eliminarItem(CarritoItem item) {
        items.remove(item);
        item.setCarrito(null);
    }

    public void vaciar() {
        items.clear();
    }
}
