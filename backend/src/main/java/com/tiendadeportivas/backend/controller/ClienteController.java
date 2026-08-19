// Gestiona el perfil, los pedidos y las facturas del cliente.
package com.tiendadeportivas.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.tiendadeportivas.backend.model.ActualizarEmailClienteRequest;
import com.tiendadeportivas.backend.model.ActualizarNombreClienteRequest;
import com.tiendadeportivas.backend.model.ActualizarPasswordClienteRequest;
import com.tiendadeportivas.backend.model.Factura;
import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.model.PedidoClienteRespuesta;
import com.tiendadeportivas.backend.model.Usuario;
import com.tiendadeportivas.backend.model.UsuarioRespuesta;
import com.tiendadeportivas.backend.service.FacturaService;
import com.tiendadeportivas.backend.service.PedidoService;
import com.tiendadeportivas.backend.service.UsuarioService;

@RestController
@RequestMapping("/cliente/perfil")
public class ClienteController {

        private final UsuarioService usuarioService;
        private final PedidoService pedidoService;
        private final FacturaService facturaService;

        // Crea una instancia de ClienteController.
        public ClienteController(
                        UsuarioService usuarioService,
                        PedidoService pedidoService,
                        FacturaService facturaService) {

                this.usuarioService = usuarioService;
                this.pedidoService = pedidoService;
                this.facturaService = facturaService;

        }

        // Actualiza el nombre del cliente autenticado.
        @PutMapping("/nombre")
        public UsuarioRespuesta actualizarNombre(
                        @Valid @RequestBody ActualizarNombreClienteRequest request,
                        Principal principal) {

                Usuario usuario = usuarioService.actualizarNombreCliente(
                                principal.getName(),
                                request);

                return new UsuarioRespuesta(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol(),
                                usuario.getFechaAlta());
        }

        // Actualiza el email del cliente autenticado.
        @PutMapping("/email")
        public UsuarioRespuesta actualizarEmail(
                        @Valid @RequestBody ActualizarEmailClienteRequest request,
                        Principal principal) {

                Usuario usuario = usuarioService.actualizarEmailCliente(
                                principal.getName(),
                                request);

                return new UsuarioRespuesta(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol(),
                                usuario.getFechaAlta());
        }

        // Actualiza la contraseña del cliente autenticado.
        @PutMapping("/password")
        public UsuarioRespuesta actualizarPassword(
                        @Valid @RequestBody ActualizarPasswordClienteRequest request,
                        Principal principal) {

                Usuario usuario = usuarioService.actualizarPasswordCliente(
                                principal.getName(),
                                request);

                return new UsuarioRespuesta(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol(),
                                usuario.getFechaAlta());
        }

        // Devuelve los pedidos del cliente autenticado.
        @GetMapping("/pedidos")
        public List<PedidoClienteRespuesta> obtenerMisPedidos(
                        Principal principal) {

                return pedidoService.obtenerPedidosCliente(
                                principal.getName());
        }

        // Devuelve el PDF de una factura del cliente.
        @GetMapping("/pedidos/{idPedido}/factura")
        public ResponseEntity<byte[]> descargarFactura(
                        @PathVariable String idPedido,
                        Principal principal) {

                Pedido pedido = pedidoService.obtenerPedidoPorIdPedido(
                                idPedido,
                                principal.getName());

                Factura factura = facturaService.obtenerFacturaCliente(
                                pedido);

                byte[] pdf = facturaService.generarPdfFacturaCliente(
                                pedido);

                String nombreArchivo = "factura-"
                                + factura.getNumeroFactura()
                                + ".pdf";

                return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_PDF)
                                .header(
                                                HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\""
                                                                + nombreArchivo
                                                                + "\"")
                                .body(pdf);
        }
}
