// Representa el mensaje de comprobación del servidor.
package com.tiendadeportivas.backend.model;

public class Saludo {

    private String mensaje;

    // Crea una instancia de Saludo.
    public Saludo(String mensaje) {
        this.mensaje = mensaje;
    }

    // Devuelve el valor de mensaje.
    public String getMensaje() {
        return mensaje;
    }

}
