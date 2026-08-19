// Aplica la lógica de clientes y empleados.
package com.tiendadeportivas.backend.service;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tiendadeportivas.backend.model.LoginRequest;
import com.tiendadeportivas.backend.model.RegistroUsuarioRequest;
import com.tiendadeportivas.backend.model.RolUsuario;
import com.tiendadeportivas.backend.model.Usuario;
import com.tiendadeportivas.backend.model.UsuarioRespuesta;
import com.tiendadeportivas.backend.model.EmpleadoRespuesta;
import com.tiendadeportivas.backend.repository.UsuarioRepository;
import com.tiendadeportivas.backend.model.CrearEmpleadoRequest;
import com.tiendadeportivas.backend.model.ActualizarEmpleadoRequest;
import com.tiendadeportivas.backend.model.ActualizarNombreClienteRequest;
import com.tiendadeportivas.backend.model.ActualizarEmailClienteRequest;
import com.tiendadeportivas.backend.model.ActualizarPasswordClienteRequest;

@Service
public class UsuarioService {

        private final UsuarioRepository usuarioRepository;
        private final PasswordEncoder passwordEncoder;

        // Crea una instancia de UsuarioService.
        public UsuarioService(
                        UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder) {

                this.usuarioRepository = usuarioRepository;
                this.passwordEncoder = passwordEncoder;
        }

        // Valida y guarda un nuevo cliente.
        public Usuario registrarCliente(RegistroUsuarioRequest request) {

                if (!request.getPassword().equals(request.getConfirmarPassword())) {
                        throw new IllegalArgumentException(
                                        "Las contraseñas no coinciden.");
                }

                String email = request.getEmail()
                                .trim()
                                .toLowerCase();

                if (usuarioRepository.existsByEmail(email)) {
                        throw new IllegalArgumentException(
                                        "Ya existe una cuenta con ese correo electrónico.");
                }

                Usuario usuario = new Usuario();

                usuario.setNombre(request.getNombre().trim());
                usuario.setEmail(email);

                usuario.setPassword(
                                passwordEncoder.encode(request.getPassword()));

                usuario.setRol(RolUsuario.CLIENTE);

                usuario.setFechaAlta(LocalDateTime.now());
                usuario.setActivo(true);

                return usuarioRepository.save(usuario);
        }

        // Devuelve los empleados gestionables.
        public List<EmpleadoRespuesta> obtenerEmpleados() {

                List<RolUsuario> rolesPersonal = List.of(
                                RolUsuario.TRABAJADOR,
                                RolUsuario.JEFE,
                                RolUsuario.ADMIN);

                return usuarioRepository
                                .findByRolInOrderByIdAsc(rolesPersonal)
                                .stream()
                                .map(usuario -> new EmpleadoRespuesta(
                                                usuario.getId(),
                                                usuario.getNombre(),
                                                usuario.getEmail(),
                                                usuario.getRol(),
                                                usuario.getFechaAlta(),
                                                usuario.isActivo()))
                                .toList();
        }

        // Valida y actualiza un empleado.
        public EmpleadoRespuesta actualizarEmpleado(
                        Long id,
                        ActualizarEmpleadoRequest request,
                        String emailUsuarioAutenticado) {

                Usuario usuarioAutenticado = usuarioRepository
                                .findByEmail(emailUsuarioAutenticado)
                                .orElseThrow(() -> new SecurityException(
                                                "No se pudo identificar al usuario autenticado."));

                if (usuarioAutenticado.getRol() != RolUsuario.ADMIN
                                && usuarioAutenticado.getRol() != RolUsuario.JEFE) {

                        throw new SecurityException(
                                        "No tienes permisos para modificar empleados.");
                }

                if (usuarioAutenticado.getId().equals(id)) {
                        throw new SecurityException(
                                        "No puedes modificar tu propio usuario desde el panel.");
                }

                Usuario usuario = usuarioRepository
                                .findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El empleado no existe."));

                if (usuarioAutenticado.getRol() == RolUsuario.JEFE
                                && usuario.getRol() != RolUsuario.TRABAJADOR) {

                        throw new SecurityException(
                                        "Un jefe solamente puede modificar trabajadores.");
                }

                if (usuario.getRol() == RolUsuario.CLIENTE) {
                        throw new IllegalArgumentException(
                                        "El usuario indicado no pertenece al personal.");
                }

                if (request.getRol() != RolUsuario.TRABAJADOR
                                && request.getRol() != RolUsuario.JEFE) {

                        throw new IllegalArgumentException(
                                        "El rol del empleado debe ser TRABAJADOR o JEFE.");
                }

                if (usuarioAutenticado.getRol() == RolUsuario.JEFE
                                && request.getRol() == RolUsuario.JEFE) {

                        throw new SecurityException(
                                        "Un jefe no puede asignar el rol JEFE.");
                }

                String email = request
                                .getEmail()
                                .trim()
                                .toLowerCase();

                usuarioRepository
                                .findByEmail(email)
                                .ifPresent(usuarioExistente -> {

                                        if (!usuarioExistente.getId().equals(id)) {

                                                throw new IllegalArgumentException(
                                                                "Ya existe una cuenta con ese correo electrónico.");
                                        }
                                });

                usuario.setNombre(
                                request.getNombre().trim());

                usuario.setEmail(email);

                usuario.setRol(
                                request.getRol());

                usuario.setActivo(
                                request.isActivo());

                String nuevaPassword = request.getPassword();

                if (nuevaPassword != null
                                && !nuevaPassword.isBlank()) {

                        if (request.getConfirmarPassword() == null
                                        || !nuevaPassword.equals(
                                                        request.getConfirmarPassword())) {

                                throw new IllegalArgumentException(
                                                "Las contraseñas no coinciden.");
                        }

                        usuario.setPassword(
                                        passwordEncoder.encode(nuevaPassword));
                }

                Usuario usuarioActualizado = usuarioRepository.save(usuario);

                return new EmpleadoRespuesta(
                                usuarioActualizado.getId(),
                                usuarioActualizado.getNombre(),
                                usuarioActualizado.getEmail(),
                                usuarioActualizado.getRol(),
                                usuarioActualizado.getFechaAlta(),
                                usuarioActualizado.isActivo());
        }

        // Valida y guarda un empleado.
        public EmpleadoRespuesta crearEmpleado(
                        CrearEmpleadoRequest request,
                        String emailUsuarioAutenticado) {

                Usuario usuarioAutenticado = usuarioRepository
                                .findByEmail(emailUsuarioAutenticado)
                                .orElseThrow(() -> new SecurityException(
                                                "No se pudo identificar al usuario autenticado."));

                if (!request.getPassword().equals(
                                request.getConfirmarPassword())) {

                        throw new IllegalArgumentException(
                                        "Las contraseñas no coinciden.");
                }

                if (request.getRol() != RolUsuario.TRABAJADOR
                                && request.getRol() != RolUsuario.JEFE) {

                        throw new IllegalArgumentException(
                                        "Solo se pueden crear empleados con rol TRABAJADOR o JEFE.");
                }

                if (usuarioAutenticado.getRol() == RolUsuario.JEFE
                                && request.getRol() != RolUsuario.TRABAJADOR) {

                        throw new SecurityException(
                                        "Un jefe solamente puede crear trabajadores.");
                }

                if (usuarioAutenticado.getRol() != RolUsuario.ADMIN
                                && usuarioAutenticado.getRol() != RolUsuario.JEFE) {

                        throw new SecurityException(
                                        "No tienes permisos para crear empleados.");
                }

                String email = request.getEmail()
                                .trim()
                                .toLowerCase();

                if (usuarioRepository.existsByEmail(email)) {

                        throw new IllegalArgumentException(
                                        "Ya existe un usuario con ese correo electrónico.");
                }

                Usuario usuario = new Usuario();

                usuario.setNombre(
                                request.getNombre().trim());

                usuario.setEmail(email);

                usuario.setPassword(
                                passwordEncoder.encode(
                                                request.getPassword()));

                usuario.setRol(
                                request.getRol());

                usuario.setActivo(
                                request.isActivo());

                usuario.setFechaAlta(
                                LocalDateTime.now());

                Usuario usuarioGuardado = usuarioRepository.save(usuario);

                return new EmpleadoRespuesta(
                                usuarioGuardado.getId(),
                                usuarioGuardado.getNombre(),
                                usuarioGuardado.getEmail(),
                                usuarioGuardado.getRol(),
                                usuarioGuardado.getFechaAlta(),
                                usuarioGuardado.isActivo());
        }

        // Autentica las credenciales recibidas.
        public UsuarioRespuesta login(LoginRequest request) {

                Usuario usuario = usuarioRepository
                                .findByEmail(request.getEmail())
                                .orElseThrow(() -> new SecurityException(
                                                "Email o contraseña incorrectos"));

                if (!passwordEncoder.matches(
                                request.getPassword(),
                                usuario.getPassword())) {

                        throw new SecurityException(
                                        "Email o contraseña incorrectos");
                }

                return new UsuarioRespuesta(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol(),
                                usuario.getFechaAlta());
        }

        // Valida y guarda el nuevo nombre del cliente.
        public Usuario actualizarNombreCliente(
                        String emailAutenticado,
                        ActualizarNombreClienteRequest request) {

                Usuario usuario = usuarioRepository
                                .findByEmail(emailAutenticado)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Usuario autenticado no encontrado."));

                usuario.setNombre(request.getNombre().trim());

                return usuarioRepository.save(usuario);
        }

        // Valida y guarda el nuevo email del cliente.
        public Usuario actualizarEmailCliente(
                        String emailAutenticado,
                        ActualizarEmailClienteRequest request) {

                Usuario usuario = usuarioRepository
                                .findByEmail(emailAutenticado)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Usuario autenticado no encontrado."));

                String nuevoEmail = request
                                .getEmail()
                                .trim()
                                .toLowerCase();

                usuarioRepository
                                .findByEmail(nuevoEmail)
                                .ifPresent(usuarioExistente -> {

                                        if (!usuarioExistente.getId().equals(usuario.getId())) {

                                                throw new IllegalArgumentException(
                                                                "Ya existe una cuenta con ese correo electrónico.");
                                        }
                                });

                usuario.setEmail(nuevoEmail);

                return usuarioRepository.save(usuario);
        }

        // Valida y guarda la nueva contraseña del cliente.
        public Usuario actualizarPasswordCliente(
                        String emailAutenticado,
                        ActualizarPasswordClienteRequest request) {

                Usuario usuario = usuarioRepository
                                .findByEmail(emailAutenticado)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Usuario autenticado no encontrado."));

                if (!request.getPassword().equals(
                                request.getConfirmarPassword())) {

                        throw new IllegalArgumentException(
                                        "Las contraseñas no coinciden.");
                }

                String passwordCodificada = passwordEncoder.encode(request.getPassword());

                usuario.setPassword(passwordCodificada);

                return usuarioRepository.save(usuario);
        }
}
