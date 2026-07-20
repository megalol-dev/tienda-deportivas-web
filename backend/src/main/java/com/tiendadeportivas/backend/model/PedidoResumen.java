package com.tiendadeportivas.backend.model;

public class PedidoResumen {
    
    private String idPedido;
    private double subtotal;
    private double iva;
    private double envio;
    private double total;

    public PedidoResumen() {
    }

    public PedidoResumen(String idPedido, double subtotal, double iva, double envio, double total) {

        this.idPedido = idPedido;
        this.subtotal = subtotal;
        this.iva = iva;
        this.envio = envio;
        this.total = total;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getEnvio() {
        return envio;
    }

    public void setEnvio(double envio) {
        this.envio = envio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
