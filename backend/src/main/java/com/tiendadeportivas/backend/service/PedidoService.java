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
import com.tiendadeportivas.backend.model.HistorialPedido;
import com.tiendadeportivas.backend.model.Producto;
import com.tiendadeportivas.backend.model.RolUsuario;
import com.tiendadeportivas.backend.model.PedidoRequest;
import com.tiendadeportivas.backend.model.PedidoResumen;
import com.tiendadeportivas.backend.repository.PedidoRepository;
import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.model.PedidoAdminResumen;
import com.tiendadeportivas.backend.repository.HistorialPedidoRepository;
import com.tiendadeportivas.backend.repository.PedidoItemRepository;
import com.tiendadeportivas.backend.model.PedidoItem;
import com.tiendadeportivas.backend.model.PedidoClienteRespuesta;
import com.tiendadeportivas.backend.model.PedidoItemClienteRespuesta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;



@Service
public class PedidoService {

    private final CarritoService carritoService;
    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistorialPedidoRepository historialPedidoRepository;

    public PedidoService(
            CarritoService carritoService,
            PedidoRepository pedidoRepository,
            PedidoItemRepository pedidoItemRepository,
            UsuarioRepository usuarioRepository,
            HistorialPedidoRepository historialPedidoRepository) {

        this.carritoService = carritoService;
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.usuarioRepository = usuarioRepository;
        this.historialPedidoRepository = historialPedidoRepository;
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

        List<CarritoItem> carrito = carritoService.obtenerItemsEntidad(emailUsuario);
        

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

                Producto producto = item.getProducto();

                subtotal = subtotal.add(
                                producto.getPrecio()
                                                .multiply(
                                                                BigDecimal.valueOf(
                                                                                item.getCantidad())));
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

                pedidoItem.setProductoId(
                                item.getProducto()
                                                .getId()
                                                .intValue());

                pedidoItem.setCantidad(item.getCantidad());
                pedidoItem.setColor(item.getColor());
                pedidoItem.setTalla(
                                String.valueOf(item.getTalla()));

                // Obtenemos directamente el producto relacionado
                // con el item persistente del carrito.

                Producto producto = item.getProducto();

                pedidoItem.setNombreProducto(
                                producto.getNombre());

                pedidoItem.setPrecioUnitario(
                                producto.getPrecio());

                pedidoItem.setSubtotalLinea(
                                producto.getPrecio()
                                                .multiply(
                                                                BigDecimal.valueOf(
                                                                                item.getCantidad())));

                pedidoItemRepository.save(pedidoItem);
        }

        // Creamos el resumen que devolveremos al frontend
        PedidoResumen resumen = new PedidoResumen();

        resumen.setIdPedido(idPedido);
        resumen.setSubtotal(subtotal.doubleValue());
        resumen.setIva(iva.doubleValue());
        resumen.setEnvio(envio.doubleValue());
        resumen.setTotal(total.doubleValue());

        // Vaciamos únicamente el carrito
        // del usuario que acaba de realizar el pedido.
        carritoService.vaciarCarrito(emailUsuario);

        return resumen;
    }

    public PedidoResumen obtenerResumenPedido() {

        // =================================================
        // OBTENER USUARIO AUTENTICADO
        // =================================================

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            throw new SecurityException(
                    "Debes iniciar sesión para consultar el resumen del pedido.");
        }

        String emailUsuario = authentication.getName();

        // =================================================
        // OBTENER SU CARRITO
        // =================================================

        List<CarritoItem> carrito = carritoService.obtenerItemsEntidad(
                        emailUsuario);
        
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CarritoItem item : carrito) {

                Producto producto = item.getProducto();

                subtotal = subtotal.add(
                                producto.getPrecio()
                                                .multiply(
                                                                BigDecimal.valueOf(
                                                                                item.getCantidad())));
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

    public List<Pedido> obtenerTodosLosPedidos() {

        return pedidoRepository.findAll();
    }

    public List<PedidoAdminResumen> obtenerResumenPedidosAdmin() {

        return pedidoRepository.findAll()
                .stream()
                .map(pedido -> new PedidoAdminResumen(
                        pedido.getId(),
                        pedido.getIdPedido(),
                        pedido.getNombre(),
                        pedido.getApellidos(),
                        pedido.getFechaPedido(),
                        pedido.getTotal(),
                        pedido.getEstado()))
                .toList();
    }

    // =====================================================
    // OBTENER PEDIDOS DEL CLIENTE AUTENTICADO
    // -----------------------------------------------------
    // El cliente no envía ningún ID.
    //
    // La identidad se obtiene desde la sesión autenticada
    // y únicamente se devuelven sus propios pedidos.
    // =====================================================

    public List<PedidoClienteRespuesta> obtenerPedidosCliente(
            String emailUsuarioAutenticado) {

        // =================================================
        // BUSCAR USUARIO AUTENTICADO
        // =================================================

        Usuario usuario = usuarioRepository
                .findByEmail(emailUsuarioAutenticado)
                .orElseThrow(() -> new SecurityException(
                        "Usuario autenticado no encontrado."));

        // =================================================
        // RECUPERAR SUS PEDIDOS
        // =================================================

        List<Pedido> pedidos = pedidoRepository
                .findByUsuarioIdOrderByFechaPedidoDesc(
                        usuario.getId());

        // =================================================
        // CONVERTIR ENTIDADES A DTO
        // =================================================

        return pedidos
                .stream()
                .map(pedido -> {

                    List<PedidoItemClienteRespuesta> items = pedido.getItems()
                            .stream()
                            .map(item -> new PedidoItemClienteRespuesta(
                                    item.getProductoId(),
                                    item.getNombreProducto(),
                                    item.getTalla(),
                                    item.getColor(),
                                    item.getCantidad(),
                                    item.getPrecioUnitario(),
                                    item.getSubtotalLinea()))
                            .toList();

                    return new PedidoClienteRespuesta(
                            pedido.getIdPedido(),
                            pedido.getFechaPedido(),
                            pedido.getEstado(),
                            pedido.getNombre(),
                            pedido.getApellidos(),
                            pedido.getEmail(),
                            pedido.getTelefono(),
                            pedido.getDireccion(),
                            pedido.getCiudad(),
                            pedido.getProvincia(),
                            pedido.getCp(),
                            pedido.getPais(),
                            pedido.getSubtotal(),
                            pedido.getIva(),
                            pedido.getEnvio(),
                            pedido.getTotal(),
                            items);
                })
                .toList();
    }

    public Pedido cambiarEstadoPedido(
            Long pedidoId,
            EstadoPedido nuevoEstado) {

        // =========================================
        // BUSCAR PEDIDO
        // =========================================

        Pedido pedido = pedidoRepository
                .findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El pedido no existe."));

        // Guardamos el estado actual antes de modificarlo
        EstadoPedido estadoAnterior = pedido.getEstado();

        // =========================================
        // OBTENER USUARIO AUTENTICADO
        // =========================================

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            throw new SecurityException(
                    "No hay ningún usuario autenticado.");
        }

        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new SecurityException(
                        "Usuario autenticado no encontrado."));

        // =========================================
        // COMPROBAR PERMISOS
        // =========================================

        if (usuario.getRol() != RolUsuario.ADMIN
                && usuario.getRol() != RolUsuario.JEFE
                && usuario.getRol() != RolUsuario.TRABAJADOR) {

            throw new SecurityException(
                    "No tienes permisos para modificar pedidos.");
        }

        // =========================================
        // COMPROBAR SI REALMENTE CAMBIÓ EL ESTADO
        // =========================================

        if (estadoAnterior == nuevoEstado) {

            // No existe ningún cambio real.
            // No modificamos el pedido.
            // No generamos un registro en el historial.

            return pedido;
        }

        // =========================================
        // CAMBIAR ESTADO
        // =========================================

        pedido.setEstado(nuevoEstado);

        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        // =========================================
        // GUARDAR HISTORIAL
        // =========================================

        HistorialPedido historial = new HistorialPedido();

        historial.setPedido(pedido);
        historial.setUsuario(usuario);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(nuevoEstado);
        historial.setFechaCambio(LocalDateTime.now());

        historialPedidoRepository.save(historial);

        return pedidoActualizado;

    }
}