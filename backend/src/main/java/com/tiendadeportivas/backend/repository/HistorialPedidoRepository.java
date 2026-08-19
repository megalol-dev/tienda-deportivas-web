// Accede al historial de pedidos almacenado.
package com.tiendadeportivas.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendadeportivas.backend.model.HistorialPedido;

public interface HistorialPedidoRepository
        extends JpaRepository<HistorialPedido, Long> {

    // Busca el historial reciente de un pedido.
    List<HistorialPedido> findByPedidoIdOrderByFechaCambioDesc(Long pedidoId);
}
