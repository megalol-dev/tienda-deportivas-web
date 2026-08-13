package com.tiendadeportivas.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.repository.ProductoRepository;
import com.tiendadeportivas.backend.model.ProductoRequest;
import java.util.ArrayList;

@Service
public class ProductoService {

    // =====================================================
    // REPOSITORIO
    // =====================================================

    private final ProductoRepository productoRepository;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // =====================================================
    // OBTENER PRODUCTOS ACTIVOS
    // -----------------------------------------------------
    // La tienda pública solamente mostrará productos
    // que tengan activo = true.
    //
    // Los productos ya no se leen desde catalogo.json.
    // Ahora se obtienen directamente desde MySQL.
    // =====================================================

    public List<Producto> obtenerProductos() {

        return productoRepository.findByActivoTrueOrderByIdAsc();
    }

    // =====================================================
    // OBTENER TODOS LOS PRODUCTOS - ADMINISTRACIÓN
    // -----------------------------------------------------
    // Devuelve tanto productos activos como inactivos.
    // =====================================================

    public List<Producto> obtenerTodosLosProductos() {

        return productoRepository.findAll();
    }

    // =====================================================
    // CREAR PRODUCTO
    // =====================================================

    public Producto crearProducto(
            ProductoRequest request) {

        // =================================================
        // VALIDACIONES ESPECÍFICAS
        // =================================================

        validarDatosProducto(request);

        // =================================================
        // CREAR ENTIDAD
        // =================================================

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

    // =====================================================
    // EDITAR PRODUCTO
    // =====================================================

    public Producto editarProducto(
            Long id,
            ProductoRequest request) {

        // =================================================
        // VALIDACIONES ESPECÍFICAS
        // =================================================

        validarDatosProducto(request);

        // =================================================
        // BUSCAR PRODUCTO
        // =================================================

        Producto producto = productoRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        // =================================================
        // ACTUALIZAR DATOS
        // =================================================

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

    // =====================================================
    // VALIDAR DATOS ESPECÍFICOS DEL PRODUCTO
    // -----------------------------------------------------
    // Complementa las validaciones de ProductoRequest.
    //
    // Aquí comprobamos especialmente los elementos
    // internos de las listas de tallas y colores.
    // =====================================================

    private void validarDatosProducto(
            ProductoRequest request) {

        // =================================================
        // TALLAS
        // =================================================

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

        // =================================================
        // COLORES
        // =================================================

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
