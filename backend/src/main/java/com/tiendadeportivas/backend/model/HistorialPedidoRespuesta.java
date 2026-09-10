// Representa de forma segura una entrada del historial de un pedido.
package com.tiendadeportivas.backend.model;

import java.time.LocalDateTime;

public class HistorialPedidoRespuesta {

    private Long id;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;
    private LocalDateTime fechaCambio;
    private TipoActorHistorial tipoActor;
    private OrigenCambioPedido origen;
    private Long usuarioId;
    private String usuarioNombre;

    // Crea una respuesta del historial de un pedido.
    public HistorialPedidoRespuesta(
            Long id,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo,
            LocalDateTime fechaCambio,
            TipoActorHistorial tipoActor,
            OrigenCambioPedido origen,
            Long usuarioId,
            String usuarioNombre) {

        this.id = id;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
        this.tipoActor = tipoActor;
        this.origen = origen;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
    }

    // Devuelve el identificador del registro.
    public Long getId() {
        return id;
    }

    // Devuelve el estado anterior del pedido.
    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }

    // Devuelve el nuevo estado del pedido.
    public EstadoPedido getEstadoNuevo() {
        return estadoNuevo;
    }

    // Devuelve la fecha del cambio.
    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    // Devuelve el tipo de actor que realizó el cambio.
    public TipoActorHistorial getTipoActor() {
        return tipoActor;
    }

    // Devuelve el origen del cambio.
    public OrigenCambioPedido getOrigen() {
        return origen;
    }

    // Devuelve el identificador del usuario que realizó el cambio.
    public Long getUsuarioId() {
        return usuarioId;
    }

    // Devuelve el nombre del usuario que realizó el cambio.
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
}