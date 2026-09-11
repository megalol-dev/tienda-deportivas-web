// Representa el detalle seguro de un pedido para el panel administrativo.
package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoAdminDetalle {

    private Long id;
    private String idPedido;
    private String nombre;
    private String apellidos;
    private LocalDateTime fechaPedido;
    private EstadoPedido estado;
    private EstadoPago estadoPago;

    private String direccion;
    private String ciudad;
    private String provincia;
    private String cp;
    private String pais;

    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal envio;
    private BigDecimal total;

    private List<PedidoItemAdminDetalle> items;

    public PedidoAdminDetalle(
            Long id,
            String idPedido,
            String nombre,
            String apellidos,
            LocalDateTime fechaPedido,
            EstadoPedido estado,
            EstadoPago estadoPago,
            String direccion,
            String ciudad,
            String provincia,
            String cp,
            String pais,
            BigDecimal subtotal,
            BigDecimal iva,
            BigDecimal envio,
            BigDecimal total,
            List<PedidoItemAdminDetalle> items) {

        this.id = id;
        this.idPedido = idPedido;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.estadoPago = estadoPago;
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

    public Long getId() {
        return id;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getCp() {
        return cp;
    }

    public String getPais() {
        return pais;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public BigDecimal getEnvio() {
        return envio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<PedidoItemAdminDetalle> getItems() {
        return items;
    }
}