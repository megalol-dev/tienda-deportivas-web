// Expone la gestión administrativa de pedidos.
package com.tiendadeportivas.backend.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.tiendadeportivas.backend.model.CambioEstadoPedidoRequest;
import com.tiendadeportivas.backend.model.CambioEstadoPedidoRespuesta;
import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.model.PedidoAdminResumen;
import com.tiendadeportivas.backend.model.HistorialPedidoRespuesta;
import com.tiendadeportivas.backend.service.PedidoService;
import com.tiendadeportivas.backend.model.PedidoAdminDetalle;

@RestController
@RequestMapping("/admin/pedidos")
public class AdminPedidoController {

    private final PedidoService pedidoService;

    // Crea una instancia de AdminPedidoController.
    public AdminPedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Devuelve los pedidos visibles para el usuario.
    @GetMapping
    public List<PedidoAdminResumen> obtenerPedidos() {

        return pedidoService.obtenerResumenPedidosAdmin();
    }

    // Devuelve el detalle de un pedido para su preparación y gestión.
    @GetMapping("/{id}")
    public PedidoAdminDetalle obtenerDetallePedido(
            @PathVariable Long id) {

        return pedidoService.obtenerDetallePedidoAdmin(id);
    }

    // Devuelve el historial de cambios de estado de un pedido.
    @GetMapping("/{id}/historial")
    public List<HistorialPedidoRespuesta> obtenerHistorial(
            @PathVariable Long id) {

        return pedidoService.obtenerHistorialPedidoAdmin(id);
    }

    // Actualiza el estado y devuelve únicamente los datos necesarios.
    @PatchMapping("/{id}/estado")
    public CambioEstadoPedidoRespuesta cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoPedidoRequest request) {

        Pedido pedido = pedidoService.cambiarEstadoPedido(
                id,
                request.getEstado(),
                request.getVersion());

        return new CambioEstadoPedidoRespuesta(
                pedido.getId(),
                pedido.getIdPedido(),
                pedido.getEstado());
    }
}
