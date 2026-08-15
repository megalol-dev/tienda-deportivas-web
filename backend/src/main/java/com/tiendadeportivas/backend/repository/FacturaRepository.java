package com.tiendadeportivas.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendadeportivas.backend.model.Factura;

@Repository
public interface FacturaRepository
        extends JpaRepository<Factura, Long> {

    // =====================================================
    // BUSCAR FACTURA POR PEDIDO
    // =====================================================

    Optional<Factura> findByPedidoId(Long pedidoId);

    // =====================================================
    // BUSCAR FACTURA POR NÚMERO
    // =====================================================

    Optional<Factura> findByNumeroFactura(
            String numeroFactura);
}
