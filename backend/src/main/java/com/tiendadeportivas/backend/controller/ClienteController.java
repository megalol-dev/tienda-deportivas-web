package com.tiendadeportivas.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendadeportivas.backend.model.Usuario;
import com.tiendadeportivas.backend.model.UsuarioRespuesta;
import com.tiendadeportivas.backend.service.UsuarioService;
import com.tiendadeportivas.backend.model.ActualizarNombreClienteRequest;
import com.tiendadeportivas.backend.model.ActualizarEmailClienteRequest;
import com.tiendadeportivas.backend.model.ActualizarPasswordClienteRequest;
import com.tiendadeportivas.backend.model.PedidoClienteRespuesta;
import com.tiendadeportivas.backend.service.PedidoService;

import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

import java.security.Principal;



// =====================================================
// CONTROLADOR DEL ÁREA PRIVADA DEL CLIENTE
// -----------------------------------------------------
// Gestiona las operaciones que un cliente puede
// realizar sobre su propia cuenta.
//
// IMPORTANTE:
// El usuario nunca indica qué ID quiere modificar.
// Spring obtiene su identidad desde la sesión.
// =====================================================

@RestController
@RequestMapping("/cliente/perfil")
public class ClienteController {

    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;

    // CONSTRUCTOR
    public ClienteController(
            UsuarioService usuarioService,
            PedidoService pedidoService) {

        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
    }

    // =====================================================
    // ACTUALIZAR NOMBRE
    // =====================================================

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

    // =====================================================
    // ACTUALIZAR EMAIL
    // -----------------------------------------------------
    // El usuario que se modifica se obtiene directamente
    // desde la sesión autenticada.
    //
    // El frontend nunca indica qué ID quiere modificar.
    // =====================================================

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

    // =====================================================
    // ACTUALIZAR CONTRASEÑA
    // -----------------------------------------------------
    // El usuario se identifica mediante la sesión.
    //
    // El frontend nunca indica qué usuario quiere
    // modificar.
    // =====================================================

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

    // =====================================================
    // OBTENER MIS PEDIDOS
    // -----------------------------------------------------
    // El cliente no proporciona ningún ID.
    //
    // Spring obtiene el usuario directamente desde
    // la sesión autenticada.
    // =====================================================

    @GetMapping("/pedidos")
    public List<PedidoClienteRespuesta> obtenerMisPedidos(
            Principal principal) {

        return pedidoService.obtenerPedidosCliente(
                principal.getName());
    }

}