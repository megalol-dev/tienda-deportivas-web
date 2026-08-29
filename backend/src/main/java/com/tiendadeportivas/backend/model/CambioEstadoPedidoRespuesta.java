package com.tiendadeportivas.backend.model;

// Expone únicamente los datos necesarios tras cambiar el estado de un pedido.
public class CambioEstadoPedidoRespuesta {

    private Long id;
    private String idPedido;
    private EstadoPedido estado;

    // Crea una respuesta limitada para el cambio de estado.
    public CambioEstadoPedidoRespuesta(
            Long id,
            String idPedido,
            EstadoPedido estado) {

        this.id = id;
        this.idPedido = idPedido;
        this.estado = estado;
    }

    // Devuelve el identificador interno del pedido.
    public Long getId() {
        return id;
    }

    // Devuelve el identificador público del pedido.
    public String getIdPedido() {
        return idPedido;
    }

    // Devuelve el estado actualizado del pedido.
    public EstadoPedido getEstado() {
        return estado;
    }
}
