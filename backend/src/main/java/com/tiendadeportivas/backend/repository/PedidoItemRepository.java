// Accede a las líneas de pedido almacenadas.
package com.tiendadeportivas.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tiendadeportivas.backend.model.PedidoItem;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

}
