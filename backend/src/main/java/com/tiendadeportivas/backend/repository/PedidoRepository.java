package com.tiendadeportivas.backend.repository;

import java.util.List;

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
}