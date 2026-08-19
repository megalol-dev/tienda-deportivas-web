// Expone la gestión administrativa de pedidos.
package com.tiendadeportivas.backend.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.tiendadeportivas.backend.model.CambioEstadoPedidoRequest;
import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.service.PedidoService;
import com.tiendadeportivas.backend.model.PedidoAdminResumen;

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

    // Actualiza el estado de un pedido.
    @PatchMapping("/{id}/estado")
    public Pedido cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoPedidoRequest request) {

        return pedidoService.cambiarEstadoPedido(
                id,
                request.getEstado());
    }
}
