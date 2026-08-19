// Representa la factura asociada a un pedido.
package com.tiendadeportivas.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", nullable = false, unique = true, length = 30)
    private String numeroFactura;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String metodoPago;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    // Crea una instancia de Factura.
    public Factura() {
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el valor de numero factura.
    public String getNumeroFactura() {
        return numeroFactura;
    }

    // Actualiza el valor de numero factura.
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    // Devuelve el valor de fecha emision.
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    // Actualiza el valor de fecha emision.
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    // Devuelve el valor de metodo pago.
    public String getMetodoPago() {
        return metodoPago;
    }

    // Actualiza el valor de metodo pago.
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
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
