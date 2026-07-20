package com.tiendadeportivas.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiendadeportivas.backend.model.CarritoItem;
import com.tiendadeportivas.backend.service.CarritoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/carrito")
    public List<CarritoItem> obtenerCarrito() {
        return carritoService.obtenerCarrito();
    }

    @PostMapping("/carrito")
    public void agregarProducto(@RequestBody CarritoItem item) {
        carritoService.agregarProducto(item);
    }

    @DeleteMapping("/carrito")
    public void eliminarProducto(
            @RequestParam int idProducto,
            @RequestParam int talla,
            @RequestParam String color) {

        carritoService.eliminarProducto(idProducto, talla, color);
    }

    @DeleteMapping("/carrito/todo")
    public void vaciarCarrito() {

        carritoService.vaciarCarrito();

    }

 

}
