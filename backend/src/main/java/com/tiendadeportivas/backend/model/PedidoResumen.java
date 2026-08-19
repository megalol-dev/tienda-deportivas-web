// Resume los importes y el identificador de un pedido.
package com.tiendadeportivas.backend.model;

public class PedidoResumen {

    private String idPedido;
    private double subtotal;
    private double iva;
    private double envio;
    private double total;

    // Crea una instancia de PedidoResumen.
    public PedidoResumen() {
    }

    // Crea una instancia de PedidoResumen.
    public PedidoResumen(String idPedido, double subtotal, double iva, double envio, double total) {

        this.idPedido = idPedido;
        this.subtotal = subtotal;
        this.iva = iva;
        this.envio = envio;
        this.total = total;
    }

    // Devuelve el identificador público del pedido.
    public String getIdPedido() {
        return idPedido;
    }

    // Actualiza el identificador público del pedido.
    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    // Devuelve el valor de subtotal.
    public double getSubtotal() {
        return subtotal;
    }

    // Actualiza el valor de subtotal.
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // Devuelve el IVA.
    public double getIva() {
        return iva;
    }

    // Actualiza el IVA.
    public void setIva(double iva) {
        this.iva = iva;
    }

    // Devuelve el valor de envio.
    public double getEnvio() {
        return envio;
    }

    // Actualiza el valor de envio.
    public void setEnvio(double envio) {
        this.envio = envio;
    }

    // Devuelve el valor de total.
    public double getTotal() {
        return total;
    }

    // Actualiza el valor de total.
    public void setTotal(double total) {
        this.total = total;
    }
}
