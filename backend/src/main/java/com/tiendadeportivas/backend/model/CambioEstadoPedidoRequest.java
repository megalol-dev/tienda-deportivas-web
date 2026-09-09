// Transporta el nuevo estado de un pedido.
package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.NotNull;

public class CambioEstadoPedidoRequest {

    @NotNull
    private EstadoPedido estado;

    @NotNull
    private Long version;

    // Crea una instancia de CambioEstadoPedidoRequest.
    public CambioEstadoPedidoRequest() {
    }

    // Devuelve el valor de estado.
    public EstadoPedido getEstado() {
        return estado;
    }

    // Actualiza el valor de estado.
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    // Devuelve la versión del pedido que vio el trabajador.
    public Long getVersion() {
        return version;
    }

    // Actualiza la versión del pedido que vio el trabajador.
    public void setVersion(Long version) {
        this.version = version;
    }
}
