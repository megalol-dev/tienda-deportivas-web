package com.tiendadeportivas.backend.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tiendadeportivas.backend.model.PedidoRequest;
import com.tiendadeportivas.backend.service.PedidoService;
import com.tiendadeportivas.backend.model.PedidoResumen;


@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public PedidoResumen crearPedido(@RequestBody PedidoRequest pedido) {

        return pedidoService.crearPedido(pedido);
    }

}
