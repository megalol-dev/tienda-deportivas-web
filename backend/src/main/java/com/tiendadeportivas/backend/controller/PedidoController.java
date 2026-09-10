// Expone la creación y el resumen de pedidos.
package com.tiendadeportivas.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

import com.tiendadeportivas.backend.model.PedidoRequest;
import com.tiendadeportivas.backend.model.PedidoResumen;
import com.tiendadeportivas.backend.service.PedidoService;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    // Crea una instancia de PedidoController.
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Calcula el resumen del carrito actual.
    @GetMapping("/resumen")
    public PedidoResumen obtenerResumenPedido() {

        return pedidoService.obtenerResumenPedido();
    }

    // Crea un pedido con el carrito autenticado.
    @PostMapping
public PedidoResumen crearPedido(
        @RequestHeader("Idempotency-Key") String idempotencyKey,
        @Valid @RequestBody PedidoRequest pedido) {

    return pedidoService.crearPedido(
            pedido,
            idempotencyKey);
}
}
