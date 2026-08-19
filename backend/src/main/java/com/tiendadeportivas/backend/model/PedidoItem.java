// Representa una línea congelada de un pedido.
package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "pedido_items")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int productoId;
    private String nombreProducto;
    private String talla;
    private String color;
    private int cantidad;
    @Column(precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotalLinea;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Crea una instancia de PedidoItem.
    public PedidoItem() {
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el identificador del producto.
    public int getProductoId() {
        return productoId;
    }

    // Actualiza el identificador del producto.
    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    // Devuelve el nombre del producto.
    public String getNombreProducto() {
        return nombreProducto;
    }

    // Actualiza el nombre del producto.
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // Devuelve el valor de talla.
    public String getTalla() {
        return talla;
    }

    // Actualiza el valor de talla.
    public void setTalla(String talla) {
        this.talla = talla;
    }

    // Devuelve el valor de color.
    public String getColor() {
        return color;
    }

    // Actualiza el valor de color.
    public void setColor(String color) {
        this.color = color;
    }

    // Devuelve el valor de cantidad.
    public int getCantidad() {
        return cantidad;
    }

    // Actualiza el valor de cantidad.
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Devuelve el precio unitario.
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    // Actualiza el precio unitario.
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    // Devuelve el subtotal de la línea.
    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }

    // Actualiza el subtotal de la línea.
    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }

    // Devuelve el valor de pedido.
    public Pedido getPedido() {
        return pedido;
    }

    // Actualiza el valor de pedido.
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
