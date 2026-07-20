package com.tiendadeportivas.backend.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.tiendadeportivas.backend.model.PedidoRequest;
import com.tiendadeportivas.backend.model.PedidoResumen;
import com.tiendadeportivas.backend.service.ProductoService;

import java.util.List;

import com.tiendadeportivas.backend.model.CarritoItem;
import com.tiendadeportivas.backend.model.Producto;

import java.math.BigDecimal;

@Service
public class PedidoService {

    private final CarritoService carritoService;
    private final ProductoService productoService;

    public PedidoService(CarritoService carritoService,
            ProductoService productoService) {

        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    public PedidoResumen crearPedido(PedidoRequest pedido) {

        List<CarritoItem> carrito = carritoService.obtenerCarrito();
        List<Producto> catalogo = productoService.obtenerProductos();

        BigDecimal subtotal = BigDecimal.ZERO;

        // Calculamos el subtotal recorriendo el carrito
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

        // Calculamos IVA
        BigDecimal iva = subtotal.multiply(BigDecimal.valueOf(0.21));

        // Gastos de envío
        BigDecimal envio;

        if (subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
            envio = BigDecimal.ZERO;
        } else {
            envio = BigDecimal.valueOf(4.99);
        }

        // Total
        BigDecimal total = subtotal.add(iva).add(envio);

        // Generamos el identificador
        String idPedido = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Creamos el resumen
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

}
