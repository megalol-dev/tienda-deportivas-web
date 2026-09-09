package com.tiendadeportivas.backend.exception;

// Indica que un pedido cambió mientras otro usuario trabajaba sobre él.
public class PedidoConcurrenteException extends RuntimeException {

    public PedidoConcurrenteException(String mensaje) {
        super(mensaje);
    }
}
