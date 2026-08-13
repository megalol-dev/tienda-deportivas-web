package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// =====================================================
// DTO DE PEDIDO PARA EL ÁREA DEL CLIENTE
// -----------------------------------------------------
// Devuelve únicamente la información necesaria para
// mostrar el histórico de pedidos del usuario.
// =====================================================

public class PedidoClienteRespuesta {

    private String idPedido;
    private LocalDateTime fechaPedido;
    private EstadoPedido estado;

    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;

    private String direccion;
    private String ciudad;
    private String provincia;
    private String cp;
    private String pais;

    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal envio;
    private BigDecimal total;

    private List<PedidoItemClienteRespuesta> items;

    public PedidoClienteRespuesta() {
    }

    public PedidoClienteRespuesta(
            String idPedido,
            LocalDateTime fechaPedido,
            EstadoPedido estado,
            String nombre,
            String apellidos,
            String email,
            String telefono,
            String direccion,
            String ciudad,
            String provincia,
            String cp,
            String pais,
            BigDecimal subtotal,
            BigDecimal iva,
            BigDecimal envio,
            BigDecimal total,
            List<PedidoItemClienteRespuesta> items) {

        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.cp = cp;
        this.pais = pais;
        this.subtotal = subtotal;
        this.iva = iva;
        this.envio = envio;
        this.total = total;
        this.items = items;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getEnvio() {
        return envio;
    }

    public void setEnvio(BigDecimal envio) {
        this.envio = envio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<PedidoItemClienteRespuesta> getItems() {
        return items;
    }

    public void setItems(List<PedidoItemClienteRespuesta> items) {
        this.items = items;
    }
}
