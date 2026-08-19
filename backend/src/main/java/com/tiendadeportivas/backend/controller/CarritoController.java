// Expone las operaciones del carrito del cliente.
package com.tiendadeportivas.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tiendadeportivas.backend.model.CarritoItemRequest;
import com.tiendadeportivas.backend.model.CarritoItemRespuesta;
import com.tiendadeportivas.backend.service.CarritoService;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@RestController
public class CarritoController {

    private final CarritoService carritoService;

    // Crea una instancia de CarritoController.
    public CarritoController(
            CarritoService carritoService) {

        this.carritoService = carritoService;
    }

    // Devuelve el carrito del cliente.
    @GetMapping("/carrito")
    public List<CarritoItemRespuesta> obtenerCarrito(
            Principal principal) {

        return carritoService.obtenerCarrito(
                principal.getName());
    }

    // Añade o incrementa un producto del carrito.
    @PostMapping("/carrito")
    public void agregarProducto(
            @RequestBody CarritoItemRequest item,
            Principal principal) {

        carritoService.agregarProducto(
                principal.getName(),
                item);
    }

    // Elimina una variante del carrito.
    @DeleteMapping("/carrito")
    public void eliminarProducto(
            @RequestParam int idProducto,
            @RequestParam int talla,
            @RequestParam String color,
            Principal principal) {

        carritoService.eliminarProducto(
                principal.getName(),
                idProducto,
                talla,
                color);
    }

    // Elimina todas las líneas del carrito.
    @DeleteMapping("/carrito/todo")
    public void vaciarCarrito(
            Principal principal) {

        carritoService.vaciarCarrito(
                principal.getName());
    }
}
