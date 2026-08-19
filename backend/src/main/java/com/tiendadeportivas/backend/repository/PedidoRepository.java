// Accede a los pedidos almacenados.
package com.tiendadeportivas.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendadeportivas.backend.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Busca los pedidos recientes de un usuario.
    List<Pedido> findByUsuarioIdOrderByFechaPedidoDesc(Long usuarioId);

    // Busca un pedido por su identificador público.
    Optional<Pedido> findByIdPedido(String idPedido);
}
