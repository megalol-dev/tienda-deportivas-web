package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.NotNull;

public class CambioEstadoPedidoRequest {

    @NotNull
    private EstadoPedido estado;

    public CambioEstadoPedidoRequest() {
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
