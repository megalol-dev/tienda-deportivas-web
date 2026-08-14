package com.tiendadeportivas.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendadeportivas.backend.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // =====================================================
    // PEDIDOS DE UN USUARIO
    // -----------------------------------------------------
    // Recupera únicamente los pedidos pertenecientes
    // al usuario indicado y muestra primero los recientes.
    // =====================================================

    List<Pedido> findByUsuarioIdOrderByFechaPedidoDesc(Long usuarioId);
    // =====================================================
    // BUSCAR PEDIDO POR SU IDENTIFICADOR PÚBLICO
    // -----------------------------------------------------
    // Ejemplo:
    // PED-A1B2C3D4
    //
    // Se utiliza para relacionar el pedido guardado
    // en nuestra base de datos con el pago de Stripe.
    // =====================================================

    Optional<Pedido> findByIdPedido(String idPedido);
}