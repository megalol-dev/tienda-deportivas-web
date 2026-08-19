// Accede a las facturas almacenadas.
package com.tiendadeportivas.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendadeportivas.backend.model.Factura;

@Repository
public interface FacturaRepository
        extends JpaRepository<Factura, Long> {

    // Busca la factura asociada a un pedido.
    Optional<Factura> findByPedidoId(Long pedidoId);

    // Busca una factura por su número.
    Optional<Factura> findByNumeroFactura(
            String numeroFactura);
}
