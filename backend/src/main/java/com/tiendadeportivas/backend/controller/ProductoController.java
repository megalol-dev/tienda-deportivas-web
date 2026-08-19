// Expone el catálogo público de productos.
package com.tiendadeportivas.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.service.ProductoService;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
public class ProductoController {

    private final ProductoService productoService;

    // Crea una instancia de ProductoController.
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Devuelve los productos disponibles.
    @GetMapping("/productos")
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

}
