// Aplica la lógica de gestión del catálogo.
package com.tiendadeportivas.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.repository.ProductoRepository;
import com.tiendadeportivas.backend.model.ProductoRequest;
import java.util.ArrayList;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Crea una instancia de ProductoService.
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Devuelve los productos disponibles.
    public List<Producto> obtenerProductos() {

        return productoRepository.findByActivoTrueOrderByIdAsc();
    }

    // Devuelve todos los productos al panel.
    public List<Producto> obtenerTodosLosProductos() {

        return productoRepository.findAll();
    }

    // Valida y guarda un producto.
    public Producto crearProducto(
            ProductoRequest request) {

        validarDatosProducto(request);

        Producto producto = new Producto();

        producto.setMarca(
                request.getMarca().trim());

        producto.setNombre(
                request.getNombre().trim());

        producto.setPrecio(
                request.getPrecio());

        producto.setTallas(
                new ArrayList<>(request.getTallas()));

        producto.setColores(
                new ArrayList<>(
                        request.getColores()
                                .stream()
                                .map(String::trim)
                                .toList()));

        producto.setActivo(
                request.isActivo());

        return productoRepository.save(producto);
    }

    // Valida y actualiza un producto.
    public Producto editarProducto(
            Long id,
            ProductoRequest request) {

        validarDatosProducto(request);

        Producto producto = productoRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        producto.setMarca(
                request.getMarca().trim());

        producto.setNombre(
                request.getNombre().trim());

        producto.setPrecio(
                request.getPrecio());

        producto.setTallas(
                new ArrayList<>(request.getTallas()));

        producto.setColores(
                new ArrayList<>(
                        request.getColores()
                                .stream()
                                .map(String::trim)
                                .toList()));

        producto.setActivo(
                request.isActivo());

        return productoRepository.save(producto);
    }

    // Comprueba los datos y variantes del producto.
    private void validarDatosProducto(
            ProductoRequest request) {

        if (request.getTallas() == null
                || request.getTallas().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debes introducir al menos una talla.");
        }

        for (Integer talla : request.getTallas()) {

            if (talla == null
                    || talla < 1
                    || talla > 100) {

                throw new IllegalArgumentException(
                        "Las tallas deben estar entre 1 y 100.");
            }
        }

        if (request.getColores() == null
                || request.getColores().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debes introducir al menos un color.");
        }

        for (String color : request.getColores()) {

            if (color == null
                    || color.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Los colores no pueden estar vacíos.");
            }

            if (color.trim().length() > 50) {

                throw new IllegalArgumentException(
                        "Cada color puede tener como máximo 50 caracteres.");
            }
        }
    }

}
