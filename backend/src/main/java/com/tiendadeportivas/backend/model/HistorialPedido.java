// Registra un cambio de estado de un pedido.
package com.tiendadeportivas.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "historial_pedidos")
public class HistorialPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = true)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_actor", nullable = false)
    private TipoActorHistorial tipoActor;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen", nullable = false)
    private OrigenCambioPedido origen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior")
    private EstadoPedido estadoAnterior;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false)
    private EstadoPedido estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    // Crea una instancia de HistorialPedido.
    public HistorialPedido() {
    }

    // Devuelve el identificador.
    public Long getId() {
        return id;
    }

    // Devuelve el valor de pedido.
    public Pedido getPedido() {
        return pedido;
    }

    // Actualiza el valor de pedido.
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    // Devuelve el valor de usuario.
    public Usuario getUsuario() {
        return usuario;
    }

    // Actualiza el valor de usuario.
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Devuelve el valor de estado anterior.
    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }

    // Actualiza el valor de estado anterior.
    public void setEstadoAnterior(EstadoPedido estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    // Devuelve el valor de estado nuevo.
    public EstadoPedido getEstadoNuevo() {
        return estadoNuevo;
    }

    // Actualiza el valor de estado nuevo.
    public void setEstadoNuevo(EstadoPedido estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    // Devuelve el valor de fecha cambio.
    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    // Actualiza el valor de fecha cambio.
    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    // Devuelve el tipo de actor que realizó el cambio.
    public TipoActorHistorial getTipoActor() {
        return tipoActor;
    }

    // Actualiza el tipo de actor que realizó el cambio.
    public void setTipoActor(TipoActorHistorial tipoActor) {
        this.tipoActor = tipoActor;
    }

    // Devuelve el origen del cambio.
    public OrigenCambioPedido getOrigen() {
        return origen;
    }

    // Actualiza el origen del cambio.
    public void setOrigen(OrigenCambioPedido origen) {
        this.origen = origen;
    }
}
