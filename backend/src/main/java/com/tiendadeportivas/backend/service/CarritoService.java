package com.tiendadeportivas.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendadeportivas.backend.model.Carrito;
import com.tiendadeportivas.backend.model.CarritoItem;
import com.tiendadeportivas.backend.model.CarritoItemRequest;
import com.tiendadeportivas.backend.model.CarritoItemRespuesta;
import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.model.Usuario;
import com.tiendadeportivas.backend.repository.CarritoItemRepository;
import com.tiendadeportivas.backend.repository.CarritoRepository;
import com.tiendadeportivas.backend.repository.ProductoRepository;
import com.tiendadeportivas.backend.repository.UsuarioRepository;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public CarritoService(
            CarritoRepository carritoRepository,
            CarritoItemRepository carritoItemRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository) {

        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    // =====================================================
    // OBTENER O CREAR CARRITO
    // =====================================================

    @Transactional
    public Carrito obtenerOCrearCarrito(
            String emailUsuario) {

        return carritoRepository
                .findByUsuarioEmail(emailUsuario)
                .orElseGet(() -> {

                    Usuario usuario = usuarioRepository
                            .findByEmail(emailUsuario)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Usuario no encontrado."));

                    Carrito carrito = new Carrito();

                    carrito.setUsuario(usuario);

                    return carritoRepository.save(carrito);
                });
    }

    // =====================================================
    // OBTENER CARRITO DEL USUARIO
    // =====================================================

    @Transactional(readOnly = true)
    public List<CarritoItemRespuesta> obtenerCarrito(
            String emailUsuario) {

        Carrito carrito = carritoRepository
                .findByUsuarioEmail(emailUsuario)
                .orElse(null);

        if (carrito == null) {
            return List.of();
        }

        return carrito.getItems()
                .stream()
                .map(item -> new CarritoItemRespuesta(
                        item.getProducto()
                                .getId()
                                .intValue(),
                        item.getTalla(),
                        item.getColor(),
                        item.getCantidad()))
                .toList();
    }

    // =====================================================
    // OBTENER ENTIDADES DEL CARRITO
    // -----------------------------------------------------
    // Se utiliza internamente para crear pedidos.
    // =====================================================

    @Transactional(readOnly = true)
    public List<CarritoItem> obtenerItemsEntidad(
            String emailUsuario) {

        Carrito carrito = carritoRepository
                .findByUsuarioEmail(emailUsuario)
                .orElse(null);

        if (carrito == null) {
            return List.of();
        }

        return List.copyOf(carrito.getItems());
    }

    // =====================================================
    // AGREGAR PRODUCTO
    // =====================================================

    @Transactional
    public void agregarProducto(
            String emailUsuario,
            CarritoItemRequest request) {

        if (request.getCantidad() <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que 0.");
        }

        Producto producto = productoRepository
                .findById((long) request.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El producto no existe."));

        if (!producto.isActivo()) {
            throw new IllegalArgumentException(
                    "El producto no está disponible.");
        }

        // =================================================
        // VALIDAR TALLA
        // =================================================

        if (!producto.getTallas()
                .contains(request.getTalla())) {

            throw new IllegalArgumentException(
                    "La talla seleccionada no es válida.");
        }

        // =================================================
        // VALIDAR COLOR
        // =================================================

        if (request.getColor() == null
                || !producto.getColores()
                        .contains(request.getColor())) {

            throw new IllegalArgumentException(
                    "El color seleccionado no es válido.");
        }

        Carrito carrito = obtenerOCrearCarrito(emailUsuario);

        carritoItemRepository
                .findByCarritoIdAndProductoIdAndTallaAndColor(
                        carrito.getId(),
                        producto.getId(),
                        request.getTalla(),
                        request.getColor())
                .ifPresentOrElse(
                        existente -> {

                            existente.setCantidad(
                                    existente.getCantidad()
                                            + request.getCantidad());

                            carritoItemRepository.save(
                                    existente);
                        },
                        () -> {

                            CarritoItem item = new CarritoItem();

                            item.setCarrito(carrito);
                            item.setProducto(producto);
                            item.setTalla(
                                    request.getTalla());
                            item.setColor(
                                    request.getColor());
                            item.setCantidad(
                                    request.getCantidad());

                            carritoItemRepository.save(item);
                        });
    }

    // =====================================================
    // ELIMINAR PRODUCTO
    // =====================================================

    @Transactional
    public void eliminarProducto(
            String emailUsuario,
            int idProducto,
            int talla,
            String color) {

        Carrito carrito = carritoRepository
                .findByUsuarioEmail(emailUsuario)
                .orElse(null);

        if (carrito == null) {
            return;
        }

        carritoItemRepository
                .findByCarritoIdAndProductoIdAndTallaAndColor(
                        carrito.getId(),
                        (long) idProducto,
                        talla,
                        color)
                .ifPresent(
                        carritoItemRepository::delete);
    }

    // =====================================================
    // VACIAR CARRITO
    // =====================================================

    @Transactional
    public void vaciarCarrito(
            String emailUsuario) {

        Carrito carrito = carritoRepository
                .findByUsuarioEmail(emailUsuario)
                .orElse(null);

        if (carrito == null) {
            return;
        }

        carrito.getItems().clear();

        carritoRepository.save(carrito);
    }
}