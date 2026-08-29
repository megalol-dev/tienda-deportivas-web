package com.tiendadeportivas.backend.controller;

import java.security.Principal;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendadeportivas.backend.model.ActualizarPasswordPersonalRequest;
import com.tiendadeportivas.backend.model.ActualizarNombreClienteRequest;
import com.tiendadeportivas.backend.model.Usuario;
import com.tiendadeportivas.backend.model.UsuarioRespuesta;
import com.tiendadeportivas.backend.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.tiendadeportivas.backend.model.ActualizarEmailPersonalRequest;

// Gestiona los cambios propios del personal sin permitir editar rol o estado.
@RestController
@RequestMapping("/admin")
public class AdminPerfilController {

    private final UsuarioService usuarioService;

    // Crea una instancia de AdminPerfilController.
    public AdminPerfilController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    // Actualiza la contraseña del miembro del personal autenticado.
    @PutMapping("/mi-password")
    public UsuarioRespuesta actualizarMiPassword(
            @Valid @RequestBody ActualizarPasswordPersonalRequest request,
            Principal principal) {

        Usuario usuario = usuarioService.actualizarPasswordPersonal(
                principal.getName(),
                request);

        return new UsuarioRespuesta(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getFechaAlta());
    }

    // Actualiza el nombre del miembro del personal autenticado.
    @PutMapping("/mi-cuenta/nombre")
    public UsuarioRespuesta actualizarMiNombre(
            @Valid @RequestBody ActualizarNombreClienteRequest request,
            Principal principal) {

        Usuario usuario = usuarioService.actualizarNombrePersonal(
                principal.getName(),
                request);

        return new UsuarioRespuesta(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getFechaAlta());
    }

    // Actualiza el email del miembro del personal autenticado.
    @PutMapping("/mi-cuenta/email")
    public UsuarioRespuesta actualizarMiEmail(
            @Valid @RequestBody ActualizarEmailPersonalRequest request,
            Principal principal,
            HttpServletRequest httpRequest) {

        Usuario usuario = usuarioService.actualizarEmailPersonal(
                principal.getName(),
                request);

        UsuarioRespuesta respuesta = new UsuarioRespuesta(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getFechaAlta());

        HttpSession session = httpRequest.getSession(false);

        // Obliga a iniciar sesión de nuevo con el email actualizado.
        if (session != null) {
            session.invalidate();
        }

        return respuesta;
    }
}
