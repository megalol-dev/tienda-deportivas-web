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

    // =====================================================
    // IDENTIFICADOR INTERNO
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // NÚMERO DE FACTURA
    // =====================================================

    @Column(name = "numero_factura", nullable = false, unique = true, length = 30)
    private String numeroFactura;

    // =====================================================
    // FECHA DE EMISIÓN
    // =====================================================

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    // =====================================================
    // MÉTODO DE PAGO
    // =====================================================

    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String metodoPago;

    // =====================================================
    // PEDIDO ASOCIADO
    // -----------------------------------------------------
    // Cada factura pertenece a un único pedido.
    // Un pedido solamente puede tener una factura.
    // =====================================================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    // =====================================================
    // CONSTRUCTOR VACÍO
    // =====================================================

    public Factura() {
    }

    // =====================================================
    // GETTERS Y SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}