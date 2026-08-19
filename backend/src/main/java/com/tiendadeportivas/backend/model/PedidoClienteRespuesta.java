// Devuelve el detalle de un pedido del cliente.
package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    // Crea una instancia de PedidoClienteRespuesta.
    public PedidoClienteRespuesta() {
    }

    // Crea una instancia de PedidoClienteRespuesta.
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

    // Devuelve el identificador público del pedido.
    public String getIdPedido() {
        return idPedido;
    }

    // Actualiza el identificador público del pedido.
    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    // Devuelve la fecha del pedido.
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    // Actualiza la fecha del pedido.
    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    // Devuelve el valor de estado.
    public EstadoPedido getEstado() {
        return estado;
    }

    // Actualiza el valor de estado.
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Actualiza el valor de nombre.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve el valor de apellidos.
    public String getApellidos() {
        return apellidos;
    }

    // Actualiza el valor de apellidos.
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    // Devuelve el valor de email.
    public String getEmail() {
        return email;
    }

    // Actualiza el valor de email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Devuelve el valor de telefono.
    public String getTelefono() {
        return telefono;
    }

    // Actualiza el valor de telefono.
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Devuelve el valor de direccion.
    public String getDireccion() {
        return direccion;
    }

    // Actualiza el valor de direccion.
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Devuelve el valor de ciudad.
    public String getCiudad() {
        return ciudad;
    }

    // Actualiza el valor de ciudad.
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    // Devuelve el valor de provincia.
    public String getProvincia() {
        return provincia;
    }

    // Actualiza el valor de provincia.
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    // Devuelve el código postal.
    public String getCp() {
        return cp;
    }

    // Actualiza el código postal.
    public void setCp(String cp) {
        this.cp = cp;
    }

    // Devuelve el valor de pais.
    public String getPais() {
        return pais;
    }

    // Actualiza el valor de pais.
    public void setPais(String pais) {
        this.pais = pais;
    }

    // Devuelve el valor de subtotal.
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    // Actualiza el valor de subtotal.
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    // Devuelve el IVA.
    public BigDecimal getIva() {
        return iva;
    }

    // Actualiza el IVA.
    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    // Devuelve el valor de envio.
    public BigDecimal getEnvio() {
        return envio;
    }

    // Actualiza el valor de envio.
    public void setEnvio(BigDecimal envio) {
        this.envio = envio;
    }

    // Devuelve el valor de total.
    public BigDecimal getTotal() {
        return total;
    }

    // Actualiza el valor de total.
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    // Devuelve el valor de items.
    public List<PedidoItemClienteRespuesta> getItems() {
        return items;
    }

    // Actualiza el valor de items.
    public void setItems(List<PedidoItemClienteRespuesta> items) {
        this.items = items;
    }
}
