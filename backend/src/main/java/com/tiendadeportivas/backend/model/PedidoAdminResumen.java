// Resume un pedido para el panel administrativo.
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

    // Crea una instancia de PedidoAdminResumen.
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

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el identificador público del pedido.
    public String getIdPedido() {
        return idPedido;
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Devuelve el valor de apellidos.
    public String getApellidos() {
        return apellidos;
    }

    // Devuelve la fecha del pedido.
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    // Devuelve el valor de total.
    public BigDecimal getTotal() {
        return total;
    }

    // Devuelve el valor de estado.
    public EstadoPedido getEstado() {
        return estado;
    }
}
