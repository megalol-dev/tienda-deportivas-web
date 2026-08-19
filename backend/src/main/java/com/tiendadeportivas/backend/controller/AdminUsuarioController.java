// Expone la gestión administrativa de empleados.
package com.tiendadeportivas.backend.controller;

import java.util.List;
import java.security.Principal;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendadeportivas.backend.model.EmpleadoRespuesta;
import com.tiendadeportivas.backend.service.UsuarioService;
import com.tiendadeportivas.backend.model.CrearEmpleadoRequest;
import com.tiendadeportivas.backend.model.ActualizarEmpleadoRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    // Crea una instancia de AdminUsuarioController.
    public AdminUsuarioController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    // Devuelve los empleados gestionables.
    @GetMapping
    public List<EmpleadoRespuesta> obtenerEmpleados() {

        return usuarioService.obtenerEmpleados();
    }

    // Valida y actualiza un empleado.
    @PutMapping("/{id}")
    public EmpleadoRespuesta actualizarEmpleado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEmpleadoRequest request,
            Principal principal) {

        return usuarioService.actualizarEmpleado(
                id,
                request,
                principal.getName());
    }

    // Valida y guarda un empleado.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpleadoRespuesta crearEmpleado(
            @Valid @RequestBody CrearEmpleadoRequest request,
            Principal principal) {

        return usuarioService.crearEmpleado(
                request,
                principal.getName());
    }
}

