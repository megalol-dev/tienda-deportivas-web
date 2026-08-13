package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoAdminResumen {

    private Long id;
    private String idPedido;
    private String nombre;
    private String apellidos;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private EstadoPedido estado;

    public PedidoAdminResumen(
            Long id,
            String idPedido,
            String nombre,
            String apellidos,
            LocalDateTime fechaPedido,
            BigDecimal total,
            EstadoPedido estado) {

        this.id = id;
        this.idPedido = idPedido;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaPedido = fechaPedido;
        this.total = total;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public EstadoPedido getEstado() {
        return estado;
    }
}
