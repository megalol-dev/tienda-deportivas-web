package com.tiendadeportivas.backend.exception;

public class ConflictoIdempotenciaException extends RuntimeException {

    public ConflictoIdempotenciaException(String mensaje) {
        super(mensaje);
    }
}
