// Aplica la lógica de pedidos, estados y pagos.
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
import com.tiendadeportivas.backend.model.EstadoPago;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.tiendadeportivas.backend.exception.PedidoConcurrenteException;

@Service
public class PedidoService {

        private final CarritoService carritoService;
        private final PedidoRepository pedidoRepository;
        private final PedidoItemRepository pedidoItemRepository;
        private final UsuarioRepository usuarioRepository;
        private final HistorialPedidoRepository historialPedidoRepository;
        private final FacturaService facturaService;

        // Crea una instancia de PedidoService.
        public PedidoService(
                        CarritoService carritoService,
                        PedidoRepository pedidoRepository,
                        PedidoItemRepository pedidoItemRepository,
                        UsuarioRepository usuarioRepository,
                        HistorialPedidoRepository historialPedidoRepository,
                        FacturaService facturaService) {

                this.carritoService = carritoService;
                this.pedidoRepository = pedidoRepository;
                this.pedidoItemRepository = pedidoItemRepository;
                this.usuarioRepository = usuarioRepository;
                this.historialPedidoRepository = historialPedidoRepository;
                this.facturaService = facturaService;
        }

        // Crea un pedido con el carrito autenticado.
        @Transactional
        public PedidoResumen crearPedido(
                        PedidoRequest request,
                        String idempotencyKey) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null
                                || !authentication.isAuthenticated()
                                || "anonymousUser".equals(authentication.getPrincipal())) {

                        throw new SecurityException(
                                        "Debes iniciar sesión para realizar un pedido.");
                }

                String emailUsuario = authentication.getName();

                Usuario usuario = usuarioRepository
                                .findByEmailForUpdate(emailUsuario)
                                .orElseThrow(() -> new SecurityException(
                                                "No se ha encontrado el usuario autenticado."));

                if (idempotencyKey == null || idempotencyKey.isBlank()) {
                        throw new IllegalArgumentException(
                                        "La clave de idempotencia es obligatoria.");
                }

                if (idempotencyKey.length() > 36) {
                        throw new IllegalArgumentException(
                                        "La clave de idempotencia no es válida.");
                }

                Pedido pedidoExistente = pedidoRepository
                                .findByUsuarioIdAndIdempotencyKey(
                                                usuario.getId(),
                                                idempotencyKey)
                                .orElse(null);

                if (pedidoExistente != null) {
                        PedidoResumen resumenExistente = new PedidoResumen();

                        resumenExistente.setIdPedido(
                                        pedidoExistente.getIdPedido());

                        resumenExistente.setSubtotal(
                                        pedidoExistente.getSubtotal().doubleValue());

                        resumenExistente.setIva(
                                        pedidoExistente.getIva().doubleValue());

                        resumenExistente.setEnvio(
                                        pedidoExistente.getEnvio().doubleValue());

                        resumenExistente.setTotal(
                                        pedidoExistente.getTotal().doubleValue());

                        return resumenExistente;
                }

                List<CarritoItem> carrito = carritoService.obtenerItemsEntidad(emailUsuario);

                if (carrito.isEmpty()) {
                        throw new IllegalStateException(
                                        "No se puede crear un pedido con el carrito vacío.");
                }

                BigDecimal subtotal = BigDecimal.ZERO;

                for (CarritoItem item : carrito) {

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

                BigDecimal iva = subtotal
                                .multiply(BigDecimal.valueOf(0.21))
                                .setScale(2, RoundingMode.HALF_UP);

                BigDecimal envio;

                if (subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
                        envio = new BigDecimal("0.00");
                } else {
                        envio = new BigDecimal("4.99");
                }

                BigDecimal total = subtotal
                                .add(iva)
                                .add(envio)
                                .setScale(2, RoundingMode.HALF_UP);

                String idPedido = "PED-" + UUID.randomUUID().toString().toUpperCase();

                Pedido pedidoEntidad = new Pedido();
                pedidoEntidad.setFechaPedido(LocalDateTime.now());
                pedidoEntidad.setEstado(EstadoPedido.PENDIENTE);
                pedidoEntidad.setEstadoPago(EstadoPago.PENDIENTE);
                pedidoEntidad.setUsuario(usuario);
                pedidoEntidad.setIdempotencyKey(idempotencyKey);
                pedidoEntidad.setIdPedido(idPedido);
                pedidoEntidad.setNombre(request.getNombre());
                pedidoEntidad.setApellidos(request.getApellidos());
                pedidoEntidad.setEmail(request.getEmail());
                pedidoEntidad.setTelefono(request.getTelefono());
                pedidoEntidad.setDireccion(request.getDireccion());
                pedidoEntidad.setCiudad(request.getCiudad());
                pedidoEntidad.setProvincia(request.getProvincia());
                pedidoEntidad.setCp(request.getCp());
                pedidoEntidad.setPais(request.getPais());
                pedidoEntidad.setSubtotal(subtotal);
                pedidoEntidad.setIva(iva);
                pedidoEntidad.setEnvio(envio);
                pedidoEntidad.setTotal(total);

                pedidoRepository.save(pedidoEntidad);

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

                PedidoResumen resumen = new PedidoResumen();

                resumen.setIdPedido(idPedido);
                resumen.setSubtotal(subtotal.doubleValue());
                resumen.setIva(iva.doubleValue());
                resumen.setEnvio(envio.doubleValue());
                resumen.setTotal(total.doubleValue());

                return resumen;
        }

        // Calcula el resumen del carrito actual.
        @Transactional(readOnly = true)
        public PedidoResumen obtenerResumenPedido() {

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

                BigDecimal iva = subtotal
                                .multiply(BigDecimal.valueOf(0.21))
                                .setScale(2, RoundingMode.HALF_UP);

                BigDecimal envio;

                if (subtotal.compareTo(BigDecimal.ZERO) == 0
                                || subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {

                        envio = new BigDecimal("0.00");

                } else {

                        envio = new BigDecimal("4.99");
                }

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

        // Devuelve todos los pedidos almacenados.
        public List<Pedido> obtenerTodosLosPedidos() {

                return pedidoRepository.findAll();
        }

        // Resume los pedidos para el panel.
        public List<PedidoAdminResumen> obtenerResumenPedidosAdmin() {

                return pedidoRepository.findAll()
                                .stream()
                                .map(pedido -> new PedidoAdminResumen(
                                                pedido.getId(),
                                                pedido.getVersion(),
                                                pedido.getIdPedido(),
                                                pedido.getNombre(),
                                                pedido.getApellidos(),
                                                pedido.getFechaPedido(),
                                                pedido.getTotal(),
                                                pedido.getEstado()))
                                .toList();
        }

        // Devuelve los pedidos de un cliente.
        @Transactional(readOnly = true)
        public List<PedidoClienteRespuesta> obtenerPedidosCliente(
                        String emailUsuarioAutenticado) {

                Usuario usuario = usuarioRepository
                                .findByEmail(emailUsuarioAutenticado)
                                .orElseThrow(() -> new SecurityException(
                                                "Usuario autenticado no encontrado."));

                List<Pedido> pedidos = pedidoRepository
                                .findByUsuarioIdOrderByFechaPedidoDesc(
                                                usuario.getId());

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
                                                        pedido.getEstado(), // Estado mostrado al cliente.
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

        // Valida y guarda un cambio de estado.
        @Transactional
        public Pedido cambiarEstadoPedido(
                        Long pedidoId,
                        EstadoPedido nuevoEstado,
                        Long versionEsperada) {

                Pedido pedido = pedidoRepository
                                .findById(pedidoId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El pedido no existe."));
                if (!pedido.getVersion().equals(versionEsperada)) {
                        throw new PedidoConcurrenteException(
                                        "El pedido ha sido modificado por otro usuario.");
                }

                EstadoPedido estadoAnterior = pedido.getEstado();

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

                if (usuario.getRol() != RolUsuario.ADMIN
                                && usuario.getRol() != RolUsuario.JEFE
                                && usuario.getRol() != RolUsuario.TRABAJADOR) {

                        throw new SecurityException(
                                        "No tienes permisos para modificar pedidos.");
                }

                if (estadoAnterior == nuevoEstado) {

                        return pedido;
                }

                if (pedido.getEstadoPago() != EstadoPago.PAGADO) {

                        throw new IllegalStateException(
                                        "No se puede gestionar un pedido que no está pagado.");
                }

                validarTransicionEstado(
                                estadoAnterior,
                                nuevoEstado);

                pedido.setEstado(nuevoEstado);

                Pedido pedidoActualizado = pedidoRepository.save(pedido);
                
                HistorialPedido historial = new HistorialPedido();

                historial.setPedido(pedido);
                historial.setUsuario(usuario);
                historial.setEstadoAnterior(estadoAnterior);
                historial.setEstadoNuevo(nuevoEstado);
                historial.setFechaCambio(LocalDateTime.now());

                historialPedidoRepository.save(historial);

                return pedidoActualizado;

        }

        // Comprueba que el cambio de estado sea válido.
        private void validarTransicionEstado(
                        EstadoPedido estadoActual,
                        EstadoPedido nuevoEstado) {

                boolean transicionValida = switch (estadoActual) {

                        case PREPARANDO ->
                                nuevoEstado == EstadoPedido.ENVIADO;

                        case ENVIADO ->
                                nuevoEstado == EstadoPedido.ENTREGADO
                                                || nuevoEstado == EstadoPedido.DEVUELTO_A_TIENDA;

                        case DEVUELTO_A_TIENDA ->
                                nuevoEstado == EstadoPedido.PREPARANDO;

                        case PENDIENTE,
                                        ENTREGADO ->
                                false;
                };

                if (!transicionValida) {

                        throw new IllegalStateException(
                                        "No se puede cambiar el pedido de "
                                                        + estadoActual
                                                        + " a "
                                                        + nuevoEstado
                                                        + ".");
                }
        }

        // Busca un pedido y comprueba su propietario.
        @Transactional(readOnly = true)
        public Pedido obtenerPedidoPorIdPedido(
                        String idPedido,
                        String emailUsuario) {

                Usuario usuario = usuarioRepository
                                .findByEmail(emailUsuario)
                                .orElseThrow(() -> new SecurityException(
                                                "Usuario autenticado no encontrado."));

                Pedido pedido = pedidoRepository
                                .findByIdPedido(idPedido)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El pedido no existe."));

                if (pedido.getUsuario() == null
                                || !pedido.getUsuario().getId().equals(usuario.getId())) {

                        throw new SecurityException(
                                        "No tienes permiso para acceder a este pedido.");
                }

                return pedido;
        }

        // Comprueba que el pedido todavía pueda iniciar un pago.
        public void validarPedidoPuedeIniciarPago(Pedido pedido) {

                if (pedido == null) {
                        throw new IllegalArgumentException(
                                        "El pedido no puede ser nulo.");
                }

                if (pedido.getEstadoPago() == EstadoPago.PAGADO) {
                        throw new IllegalStateException(
                                        "El pedido ya está pagado.");
                }
        }

        // Asocia una sesión de Stripe al pedido.
        @Transactional
        public void guardarStripeSessionId(
                        Pedido pedido,
                        String stripeSessionId) {

                if (pedido == null) {
                        throw new IllegalArgumentException(
                                        "El pedido no puede ser nulo.");
                }

                if (stripeSessionId == null || stripeSessionId.isBlank()) {
                        throw new IllegalArgumentException(
                                        "El identificador de Stripe no puede estar vacío.");
                }

                pedido.setStripeSessionId(stripeSessionId);

                pedidoRepository.save(pedido);
        }

        // Confirma el pago y completa sus efectos asociados.
        @Transactional
        public void confirmarPagoStripe(
                        String stripeSessionId,
                        String idPedido) {

                if (stripeSessionId == null || stripeSessionId.isBlank()) {
                        throw new IllegalArgumentException(
                                        "El identificador de Stripe no puede estar vacío.");
                }

                if (idPedido == null || idPedido.isBlank()) {
                        throw new IllegalArgumentException(
                                        "El identificador del pedido no puede estar vacío.");
                }

                Pedido pedido = pedidoRepository
                                .findByIdPedido(idPedido)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No existe el pedido asociado al pago de Stripe."));

                if (pedido.getStripeSessionId() == null
                                || !pedido.getStripeSessionId().equals(stripeSessionId)) {

                        throw new IllegalStateException(
                                        "La sesión de Stripe no coincide con el pedido.");
                }

                if (pedido.getEstadoPago() == EstadoPago.PAGADO) {
                        return;
                }

                if (pedido.getEstado() != EstadoPedido.PENDIENTE
                                || pedido.getEstadoPago() != EstadoPago.PENDIENTE) {

                        throw new IllegalStateException(
                                        "El pedido no se encuentra en un estado válido para confirmar el pago.");
                }

                pedido.setEstadoPago(EstadoPago.PAGADO);
                pedido.setEstado(EstadoPedido.PREPARANDO);

                pedidoRepository.save(pedido);

                facturaService.crearFactura(pedido);

                if (pedido.getUsuario() != null) {

                        carritoService.vaciarCarrito(
                                        pedido.getUsuario().getEmail());
                }

                if (pedido.getUsuario() != null) {

                        carritoService.vaciarCarrito(
                                        pedido.getUsuario().getEmail());
                }
        }
}
