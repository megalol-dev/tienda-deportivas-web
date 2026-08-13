package com.tiendadeportivas.backend.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.service.ProductoService;
import com.tiendadeportivas.backend.model.ProductoRequest;

@RestController
@RequestMapping("/admin/productos")
public class AdminProductoController {

    private final ProductoService productoService;

    public AdminProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // =====================================================
    // LISTAR TODOS LOS PRODUCTOS
    // =====================================================

    @GetMapping
    public List<Producto> obtenerProductos() {

        return productoService.obtenerTodosLosProductos();
    }

    // =====================================================
    // CREAR PRODUCTO
    // =====================================================

    @PostMapping
    public ResponseEntity<Producto> crearProducto(
            @Valid @RequestBody ProductoRequest request) {

        Producto productoCreado = productoService.crearProducto(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoCreado);
    }

    // =====================================================
    // EDITAR PRODUCTO
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<Producto> editarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {

        Producto productoActualizado = productoService.editarProducto(
                id,
                request);

        return ResponseEntity.ok(productoActualizado);
    }
}
