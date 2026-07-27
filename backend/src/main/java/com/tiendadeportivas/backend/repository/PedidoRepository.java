package com.tiendadeportivas.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tiendadeportivas.backend.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
