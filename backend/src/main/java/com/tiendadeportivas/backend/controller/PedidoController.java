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

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/resumen")
    public PedidoResumen obtenerResumenPedido() {

        return pedidoService.obtenerResumenPedido();
    }

    @PostMapping
    public PedidoResumen crearPedido(
            @Valid @RequestBody PedidoRequest pedido) {

        return pedidoService.crearPedido(pedido);
    }
}
