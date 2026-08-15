package com.tiendadeportivas.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendadeportivas.backend.model.EstadoPago;
import com.tiendadeportivas.backend.model.Factura;
import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.repository.FacturaRepository;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final FacturaPdfService facturaPdfService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public FacturaService(
            FacturaRepository facturaRepository,
            FacturaPdfService facturaPdfService) {

        this.facturaRepository = facturaRepository;
        this.facturaPdfService = facturaPdfService;
    }

    // =====================================================
    // CREAR FACTURA
    // -----------------------------------------------------
    // La factura solamente puede generarse cuando Stripe
    // ya ha confirmado que el pedido está pagado.
    // =====================================================

    @Transactional
    public Factura crearFactura(Pedido pedido) {

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido no puede ser nulo.");
        }

        if (pedido.getId() == null) {
            throw new IllegalArgumentException(
                    "El pedido debe estar guardado antes de crear la factura.");
        }

        if (pedido.getEstadoPago() != EstadoPago.PAGADO) {
            throw new IllegalStateException(
                    "No se puede generar una factura para un pedido no pagado.");
        }

        // =================================================
        // EVITAR FACTURAS DUPLICADAS
        // -------------------------------------------------
        // Los webhooks pueden recibirse más de una vez.
        // Si ya existe una factura, devolvemos la existente.
        // =================================================

        return facturaRepository
                .findByPedidoId(pedido.getId())
                .orElseGet(() -> {

                    Factura factura = new Factura();

                    factura.setNumeroFactura(
                            generarNumeroFactura(pedido));

                    factura.setFechaEmision(
                            LocalDateTime.now());

                    factura.setMetodoPago(
                            "TARJETA");

                    factura.setPedido(pedido);

                    return facturaRepository.save(factura);
                });
    }

    // =====================================================
    // OBTENER FACTURA DE UN PEDIDO
    // =====================================================

    @Transactional(readOnly = true)
    public Factura obtenerFacturaPorPedido(
            Long pedidoId) {

        return facturaRepository
                .findByPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una factura para este pedido."));
    }

    // =====================================================
    // OBTENER FACTURA DEL CLIENTE
    // -----------------------------------------------------
    // Devuelve la factura únicamente después de que el
    // PedidoService haya comprobado que el pedido pertenece
    // al usuario autenticado.
    // =====================================================

    @Transactional(readOnly = true)
    public Factura obtenerFacturaCliente(
            Pedido pedido) {

        if (pedido == null) {

            throw new IllegalArgumentException(
                    "El pedido no puede ser nulo.");
        }

        if (pedido.getEstadoPago() != EstadoPago.PAGADO) {

            throw new IllegalStateException(
                    "La factura solo está disponible para pedidos pagados.");
        }

        return facturaRepository
                .findByPedidoId(pedido.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una factura para este pedido."));
    }

    // =====================================================
    // GENERAR NÚMERO DE FACTURA
    // -----------------------------------------------------
    // De momento utilizamos el identificador interno del
    // pedido para garantizar un número único y estable.
    //
    // Ejemplo:
    // FAC-2026-000015
    // =====================================================

    private String generarNumeroFactura(
            Pedido pedido) {

        int anio = LocalDateTime.now().getYear();

        return String.format(
                "FAC-%d-%06d",
                anio,
                pedido.getId());
    }

    // =====================================================
    // GENERAR PDF DE FACTURA DEL CLIENTE
    // -----------------------------------------------------
    // La generación se realiza dentro de la transacción
    // para mantener disponibles las relaciones LAZY
    // necesarias del pedido y sus items.
    // =====================================================

    @Transactional(readOnly = true)
    public byte[] generarPdfFacturaCliente(
            Pedido pedido) {

        if (pedido == null) {

            throw new IllegalArgumentException(
                    "El pedido no puede ser nulo.");
        }

        if (pedido.getEstadoPago() != EstadoPago.PAGADO) {

            throw new IllegalStateException(
                    "La factura solo está disponible para pedidos pagados.");
        }

        Factura factura = facturaRepository
                .findByPedidoId(pedido.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una factura para este pedido."));

        return facturaPdfService.generarPdf(
                factura);
    }
}
