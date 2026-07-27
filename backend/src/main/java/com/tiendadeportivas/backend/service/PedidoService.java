package com.tiendadeportivas.backend.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tiendadeportivas.backend.model.Usuario;
import com.tiendadeportivas.backend.repository.UsuarioRepository;

import java.util.List;

import com.tiendadeportivas.backend.model.CarritoItem;
import com.tiendadeportivas.backend.model.EstadoPedido;
import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.model.PedidoRequest;
import com.tiendadeportivas.backend.model.PedidoResumen;
import com.tiendadeportivas.backend.repository.PedidoRepository;
import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.repository.PedidoItemRepository;
import com.tiendadeportivas.backend.model.PedidoItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class PedidoService {

    private final CarritoService carritoService;
    private final ProductoService productoService;
    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(
            CarritoService carritoService,
            ProductoService productoService,
            PedidoRepository pedidoRepository,
            PedidoItemRepository pedidoItemRepository,
            UsuarioRepository usuarioRepository) {

        this.carritoService = carritoService;
        this.productoService = productoService;
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public PedidoResumen crearPedido(PedidoRequest pedido) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            throw new SecurityException(
                    "Debes iniciar sesión para realizar un pedido.");
        }

        String emailUsuario = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByEmail(emailUsuario)
                .orElseThrow(() -> new SecurityException(
                        "No se ha encontrado el usuario autenticado."));

        List<CarritoItem> carrito = carritoService.obtenerCarrito();
        List<Producto> catalogo = productoService.obtenerProductos();

        // No permitimos crear pedidos con el carrito vacío
        if (carrito.isEmpty()) {
            throw new IllegalStateException(
                    "No se puede crear un pedido con el carrito vacío.");
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        // Calculamos el subtotal
        for (CarritoItem item : carrito) {

            // Validamos la cantidad
            if (item.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad de un producto debe ser mayor que 0.");
            }

            boolean productoEncontrado = false;

            for (Producto producto : catalogo) {

                if (producto.getId() == item.getIdProducto()) {

                    productoEncontrado = true;

                    subtotal = subtotal.add(
                            producto.getPrecio().multiply(
                                    BigDecimal.valueOf(item.getCantidad())));

                    break;
                }
            }

            // El producto del carrito debe existir realmente
            if (!productoEncontrado) {
                throw new IllegalArgumentException(
                        "El producto con ID " + item.getIdProducto()
                                + " no existe en el catálogo.");
            }
        }

        // IVA
        BigDecimal iva = subtotal
                .multiply(BigDecimal.valueOf(0.21))
                .setScale(2, RoundingMode.HALF_UP);

        // Envío
        BigDecimal envio;

        if (subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
            envio = new BigDecimal("0.00");
        } else {
            envio = new BigDecimal("4.99");
        }

        // Total
        BigDecimal total = subtotal
                .add(iva)
                .add(envio)
                .setScale(2, RoundingMode.HALF_UP);

        // ID del pedido
        String idPedido = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Creamos la entidad Pedido
        Pedido pedidoEntidad = new Pedido();
        pedidoEntidad.setFechaPedido(LocalDateTime.now());
        pedidoEntidad.setEstado(EstadoPedido.PENDIENTE);
        pedidoEntidad.setUsuario(usuario);

        pedidoEntidad.setIdPedido(idPedido);
        pedidoEntidad.setNombre(pedido.getNombre());
        pedidoEntidad.setApellidos(pedido.getApellidos());
        pedidoEntidad.setEmail(pedido.getEmail());
        pedidoEntidad.setTelefono(pedido.getTelefono());
        pedidoEntidad.setDireccion(pedido.getDireccion());
        pedidoEntidad.setCiudad(pedido.getCiudad());
        pedidoEntidad.setProvincia(pedido.getProvincia());
        pedidoEntidad.setCp(pedido.getCp());
        pedidoEntidad.setPais(pedido.getPais());

        pedidoEntidad.setSubtotal(subtotal);
        pedidoEntidad.setIva(iva);
        pedidoEntidad.setEnvio(envio);
        pedidoEntidad.setTotal(total);

        // Guardamos el pedido
        pedidoRepository.save(pedidoEntidad);

        // Guardamos cada producto del pedido
        for (CarritoItem item : carrito) {

            PedidoItem pedidoItem = new PedidoItem();

            pedidoItem.setPedido(pedidoEntidad);

            pedidoItem.setProductoId(item.getIdProducto());
            pedidoItem.setCantidad(item.getCantidad());
            pedidoItem.setColor(item.getColor());
            pedidoItem.setTalla(String.valueOf(item.getTalla()));

            // Buscamos el producto para obtener nombre y precio
            for (Producto producto : catalogo) {

                if (producto.getId() == item.getIdProducto()) {

                    pedidoItem.setNombreProducto(producto.getNombre());

                    pedidoItem.setPrecioUnitario(producto.getPrecio());

                    pedidoItem.setSubtotalLinea(
                            producto.getPrecio()
                                    .multiply(BigDecimal.valueOf(item.getCantidad())));

                    break;
                }
            }

            pedidoItemRepository.save(pedidoItem);
        }

        // Creamos el resumen que devolveremos al frontend
        PedidoResumen resumen = new PedidoResumen();

        resumen.setIdPedido(idPedido);
        resumen.setSubtotal(subtotal.doubleValue());
        resumen.setIva(iva.doubleValue());
        resumen.setEnvio(envio.doubleValue());
        resumen.setTotal(total.doubleValue());

        // Vaciamos el carrito
        carritoService.vaciarCarrito();

        return resumen;
    }

    public PedidoResumen obtenerResumenPedido() {

        List<CarritoItem> carrito = carritoService.obtenerCarrito();
        List<Producto> catalogo = productoService.obtenerProductos();

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CarritoItem item : carrito) {

            for (Producto producto : catalogo) {

                if (producto.getId() == item.getIdProducto()) {

                    subtotal = subtotal.add(
                            producto.getPrecio().multiply(
                                    BigDecimal.valueOf(item.getCantidad())));

                    break;
                }
            }
        }

        // IVA
        BigDecimal iva = subtotal
                .multiply(BigDecimal.valueOf(0.21))
                .setScale(2, RoundingMode.HALF_UP);

        // Envío
        BigDecimal envio;

        if (subtotal.compareTo(BigDecimal.ZERO) == 0
                || subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {

            envio = new BigDecimal("0.00");

        } else {

            envio = new BigDecimal("4.99");
        }

        // Total
        BigDecimal total = subtotal
                .add(iva)
                .add(envio)
                .setScale(2, RoundingMode.HALF_UP);

        PedidoResumen resumen = new PedidoResumen();

        resumen.setSubtotal(subtotal.doubleValue());
        resumen.setIva(iva.doubleValue());
        resumen.setEnvio(envio.doubleValue());
        resumen.setTotal(total.doubleValue());

        return resumen;
    }
}