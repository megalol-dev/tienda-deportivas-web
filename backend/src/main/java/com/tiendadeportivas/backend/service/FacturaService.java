// Crea y recupera facturas de pedidos pagados.
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

    // Crea una instancia de FacturaService.
    public FacturaService(
            FacturaRepository facturaRepository,
            FacturaPdfService facturaPdfService) {

        this.facturaRepository = facturaRepository;
        this.facturaPdfService = facturaPdfService;
    }

    // Crea la factura de un pedido pagado.
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

    // Busca la factura asociada a un pedido.
    @Transactional(readOnly = true)
    public Factura obtenerFacturaPorPedido(
            Long pedidoId) {

        return facturaRepository
                .findByPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una factura para este pedido."));
    }

    // Devuelve una factura si pertenece al cliente.
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

    // Genera el número único de factura.
    private String generarNumeroFactura(
            Pedido pedido) {

        int anio = LocalDateTime.now().getYear();

        return String.format(
                "FAC-%d-%06d",
                anio,
                pedido.getId());
    }

    // Genera el PDF de una factura del cliente.
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
