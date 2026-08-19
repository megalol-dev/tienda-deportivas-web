// Representa un pedido persistente.
package com.tiendadeportivas.backend.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idPedido;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String provincia;
    private String cp;
    private String pais;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal iva;

    @Column(precision = 10, scale = 2)
    private BigDecimal envio;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", length = 30)
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    @Column(name = "stripe_session_id", length = 255)
    private String stripeSessionId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Crea una instancia de Pedido.
    public Pedido() {
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el identificador público del pedido.
    public String getIdPedido() {
        return idPedido;
    }

    // Actualiza el identificador público del pedido.
    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
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

    // Devuelve el estado del pago.
    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    // Actualiza el estado del pago.
    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    // Devuelve el identificador de sesión de Stripe.
    public String getStripeSessionId() {
        return stripeSessionId;
    }

    // Actualiza el identificador de sesión de Stripe.
    public void setStripeSessionId(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }

    // Devuelve el valor de items.
    public List<PedidoItem> getItems() {
        return items;
    }

    // Actualiza el valor de items.
    public void setItems(List<PedidoItem> items) {
        this.items = items;
    }

    // Añade una línea y enlaza su propietario.
    public void agregarItem(PedidoItem item) {
        items.add(item);
        item.setPedido(this);
    }

    // Devuelve el valor de usuario.
    public Usuario getUsuario() {
        return usuario;
    }

    // Actualiza el valor de usuario.
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
