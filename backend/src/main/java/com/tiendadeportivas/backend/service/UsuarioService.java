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

        public UsuarioService(
                        UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder) {

                this.usuarioRepository = usuarioRepository;
                this.passwordEncoder = passwordEncoder;
        }

        public Usuario registrarCliente(RegistroUsuarioRequest request) {

                // 1. Comprobamos que las contraseñas coinciden
                if (!request.getPassword().equals(request.getConfirmarPassword())) {
                        throw new IllegalArgumentException(
                                        "Las contraseñas no coinciden.");
                }

                // 2. Normalizamos el email
                String email = request.getEmail()
                                .trim()
                                .toLowerCase();

                // 3. Comprobamos que el email no esté registrado
                if (usuarioRepository.existsByEmail(email)) {
                        throw new IllegalArgumentException(
                                        "Ya existe una cuenta con ese correo electrónico.");
                }

                // 4. Creamos el usuario
                Usuario usuario = new Usuario();

                usuario.setNombre(request.getNombre().trim());
                usuario.setEmail(email);

                // 5. Ciframos la contraseña antes de guardarla
                usuario.setPassword(
                                passwordEncoder.encode(request.getPassword()));

                // 6. Todo registro público será CLIENTE
                usuario.setRol(RolUsuario.CLIENTE);

                // 7. Guardamos fecha de creación y activamos la cuenta
                usuario.setFechaAlta(LocalDateTime.now());
                usuario.setActivo(true);

                // 8. Guardamos el usuario en MariaDB
                return usuarioRepository.save(usuario);
        }

        // =====================================================
        // OBTENER EMPLEADOS
        // -----------------------------------------------------
        // Recupera únicamente usuarios pertenecientes
        // al personal y los convierte a un DTO seguro.
        // =====================================================

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

        // =====================================================
        // ACTUALIZAR EMPLEADO
        // -----------------------------------------------------
        // Modifica los datos administrativos de un empleado.
        //
        // La contraseña no se modifica desde esta operación.
        // =====================================================

        public EmpleadoRespuesta actualizarEmpleado(
                        Long id,
                        ActualizarEmpleadoRequest request,
                        String emailUsuarioAutenticado) {

                // =================================================
                // BUSCAR EMPLEADO
                // =================================================

                Usuario usuarioAutenticado = usuarioRepository
                                .findByEmail(emailUsuarioAutenticado)
                                .orElseThrow(() -> new SecurityException(
                                                "No se pudo identificar al usuario autenticado."));

                // =================================================
                // COMPROBAR PERMISOS DEL USUARIO AUTENTICADO
                // =================================================

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

                // =================================================
                // JERARQUÍA DE EDICIÓN
                // -------------------------------------------------
                // Un JEFE solamente puede modificar
                // usuarios con rol TRABAJADOR.
                //
                // Por tanto, un JEFE no puede modificar:
                // • ADMIN
                // • JEFE
                // =================================================

                if (usuarioAutenticado.getRol() == RolUsuario.JEFE
                                && usuario.getRol() != RolUsuario.TRABAJADOR) {

                        throw new SecurityException(
                                        "Un jefe solamente puede modificar trabajadores.");
                }

                // =================================================
                // COMPROBAR QUE PERTENECE AL PERSONAL
                // =================================================

                if (usuario.getRol() == RolUsuario.CLIENTE) {
                        throw new IllegalArgumentException(
                                        "El usuario indicado no pertenece al personal.");
                }

                // =================================================
                // VALIDAR ROL NUEVO
                // -------------------------------------------------
                // Desde este formulario solamente permitimos
                // TRABAJADOR o JEFE.
                // =================================================

                if (request.getRol() != RolUsuario.TRABAJADOR
                                && request.getRol() != RolUsuario.JEFE) {

                        throw new IllegalArgumentException(
                                        "El rol del empleado debe ser TRABAJADOR o JEFE.");
                }

                // =================================================
                // IMPEDIR ASCENSOS A JEFE REALIZADOS POR UN JEFE
                // -------------------------------------------------
                // Solamente ADMIN puede asignar el rol JEFE.
                // =================================================

                if (usuarioAutenticado.getRol() == RolUsuario.JEFE
                                && request.getRol() == RolUsuario.JEFE) {

                        throw new SecurityException(
                                        "Un jefe no puede asignar el rol JEFE.");
                }

                // =================================================
                // NORMALIZAR EMAIL
                // =================================================

                String email = request
                                .getEmail()
                                .trim()
                                .toLowerCase();

                // =================================================
                // COMPROBAR EMAIL
                // -------------------------------------------------
                // Si el email pertenece a otro usuario,
                // no permitimos realizar el cambio.
                // =================================================

                usuarioRepository
                                .findByEmail(email)
                                .ifPresent(usuarioExistente -> {

                                        if (!usuarioExistente.getId().equals(id)) {

                                                throw new IllegalArgumentException(
                                                                "Ya existe una cuenta con ese correo electrónico.");
                                        }
                                });

                // =================================================
                // ACTUALIZAR DATOS
                // =================================================

                usuario.setNombre(
                                request.getNombre().trim());

                usuario.setEmail(email);

                usuario.setRol(
                                request.getRol());

                usuario.setActivo(
                                request.isActivo());

                // =================================================
                // ACTUALIZAR CONTRASEÑA SOLO SI SE HA INTRODUCIDO
                // -------------------------------------------------
                // Si password está vacío, conservamos el hash
                // existente.
                //
                // Si contiene una nueva contraseña:
                // 1. Comprobamos la confirmación.
                // 2. La ciframos con BCrypt.
                // 3. Sustituimos el hash anterior.
                // =================================================

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

                // =================================================
                // DEVOLVER DTO SEGURO
                // =================================================

                return new EmpleadoRespuesta(
                                usuarioActualizado.getId(),
                                usuarioActualizado.getNombre(),
                                usuarioActualizado.getEmail(),
                                usuarioActualizado.getRol(),
                                usuarioActualizado.getFechaAlta(),
                                usuarioActualizado.isActivo());
        }

        // =====================================================
        // CREAR EMPLEADO
        // -----------------------------------------------------
        // Permite crear únicamente usuarios pertenecientes
        // al personal.
        //
        // Desde esta operación administrativa solamente
        // pueden crearse:
        //
        // • TRABAJADOR
        // • JEFE
        //
        // No permitimos crear ADMIN ni CLIENTE.
        // =====================================================

        public EmpleadoRespuesta crearEmpleado(
                        CrearEmpleadoRequest request,
                        String emailUsuarioAutenticado) {

                // =============================================
                // IDENTIFICAR AL USUARIO QUE CREA EL EMPLEADO
                // =============================================

                Usuario usuarioAutenticado = usuarioRepository
                                .findByEmail(emailUsuarioAutenticado)
                                .orElseThrow(() -> new SecurityException(
                                                "No se pudo identificar al usuario autenticado."));

                // =============================================
                // COMPROBAR CONTRASEÑAS
                // =============================================

                if (!request.getPassword().equals(
                                request.getConfirmarPassword())) {

                        throw new IllegalArgumentException(
                                        "Las contraseñas no coinciden.");
                }

                // =============================================
                // VALIDAR ROL SOLICITADO
                // ---------------------------------------------
                // Nunca permitimos crear ADMIN ni CLIENTE
                // desde la gestión de empleados.
                // =============================================

                if (request.getRol() != RolUsuario.TRABAJADOR
                                && request.getRol() != RolUsuario.JEFE) {

                        throw new IllegalArgumentException(
                                        "Solo se pueden crear empleados con rol TRABAJADOR o JEFE.");
                }

                // =============================================
                // JERARQUÍA DE CREACIÓN
                // ---------------------------------------------
                // JEFE:
                // solamente puede crear TRABAJADORES.
                //
                // ADMIN:
                // puede crear TRABAJADORES y JEFES.
                // =============================================

                if (usuarioAutenticado.getRol() == RolUsuario.JEFE
                                && request.getRol() != RolUsuario.TRABAJADOR) {

                        throw new SecurityException(
                                        "Un jefe solamente puede crear trabajadores.");
                }

                // =============================================
                // SEGURIDAD ADICIONAL
                // =============================================

                if (usuarioAutenticado.getRol() != RolUsuario.ADMIN
                                && usuarioAutenticado.getRol() != RolUsuario.JEFE) {

                        throw new SecurityException(
                                        "No tienes permisos para crear empleados.");
                }

                // =============================================
                // NORMALIZAR EMAIL
                // =============================================

                String email = request.getEmail()
                                .trim()
                                .toLowerCase();

                // =============================================
                // COMPROBAR EMAIL DUPLICADO
                // =============================================

                if (usuarioRepository.existsByEmail(email)) {

                        throw new IllegalArgumentException(
                                        "Ya existe un usuario con ese correo electrónico.");
                }

                // =============================================
                // CREAR USUARIO
                // =============================================

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

                // =============================================
                // GUARDAR EN BASE DE DATOS
                // =============================================

                Usuario usuarioGuardado = usuarioRepository.save(usuario);

                // =============================================
                // DEVOLVER DTO SEGURO
                // =============================================

                return new EmpleadoRespuesta(
                                usuarioGuardado.getId(),
                                usuarioGuardado.getNombre(),
                                usuarioGuardado.getEmail(),
                                usuarioGuardado.getRol(),
                                usuarioGuardado.getFechaAlta(),
                                usuarioGuardado.isActivo());
        }

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

        // =====================================================
        // ACTUALIZAR NOMBRE DEL CLIENTE
        // -----------------------------------------------------
        // El usuario se identifica mediante el email obtenido
        // de su sesión autenticada.
        //
        // Nunca recibimos un ID desde el frontend, evitando
        // que un cliente pueda intentar modificar a otro.
        // =====================================================

        public Usuario actualizarNombreCliente(
                        String emailAutenticado,
                        ActualizarNombreClienteRequest request) {

                // =================================================
                // BUSCAR USUARIO AUTENTICADO
                // =================================================

                Usuario usuario = usuarioRepository
                                .findByEmail(emailAutenticado)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Usuario autenticado no encontrado."));

                // =================================================
                // ACTUALIZAR ÚNICAMENTE EL NOMBRE
                // =================================================

                usuario.setNombre(request.getNombre().trim());

                // =================================================
                // GUARDAR EN BASE DE DATOS
                // =================================================

                return usuarioRepository.save(usuario);
        }

        // =====================================================
        // ACTUALIZAR EMAIL DEL CLIENTE
        // -----------------------------------------------------
        // El usuario se identifica mediante el email obtenido
        // de su sesión autenticada.
        //
        // El nuevo email:
        // • Se normaliza.
        // • Debe ser único.
        // • Nunca permite modificar otra cuenta.
        // =====================================================

        public Usuario actualizarEmailCliente(
                        String emailAutenticado,
                        ActualizarEmailClienteRequest request) {

                // =================================================
                // BUSCAR USUARIO AUTENTICADO
                // =================================================

                Usuario usuario = usuarioRepository
                                .findByEmail(emailAutenticado)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Usuario autenticado no encontrado."));

                // =================================================
                // NORMALIZAR NUEVO EMAIL
                // =================================================

                String nuevoEmail = request
                                .getEmail()
                                .trim()
                                .toLowerCase();

                // =================================================
                // COMPROBAR SI EL EMAIL PERTENECE A OTRA CUENTA
                // -------------------------------------------------
                // Si encontramos ese email:
                //
                // • Si pertenece al mismo usuario, no hay conflicto.
                // • Si pertenece a otro usuario, rechazamos el cambio.
                // =================================================

                usuarioRepository
                                .findByEmail(nuevoEmail)
                                .ifPresent(usuarioExistente -> {

                                        if (!usuarioExistente.getId().equals(usuario.getId())) {

                                                throw new IllegalArgumentException(
                                                                "Ya existe una cuenta con ese correo electrónico.");
                                        }
                                });

                // =================================================
                // ACTUALIZAR EMAIL
                // =================================================

                usuario.setEmail(nuevoEmail);

                // =================================================
                // GUARDAR EN BASE DE DATOS
                // =================================================

                return usuarioRepository.save(usuario);
        }

        // =====================================================
        // ACTUALIZAR CONTRASEÑA DEL CLIENTE
        // -----------------------------------------------------
        // El usuario se identifica mediante el email obtenido
        // de su sesión autenticada.
        //
        // La nueva contraseña nunca se almacena en texto plano.
        // Se codifica mediante PasswordEncoder antes de
        // guardarla en la base de datos.
        // =====================================================

        public Usuario actualizarPasswordCliente(
                        String emailAutenticado,
                        ActualizarPasswordClienteRequest request) {

                // =================================================
                // BUSCAR USUARIO AUTENTICADO
                // =================================================

                Usuario usuario = usuarioRepository
                                .findByEmail(emailAutenticado)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Usuario autenticado no encontrado."));

                // =================================================
                // COMPROBAR QUE LAS CONTRASEÑAS COINCIDEN
                // =================================================

                if (!request.getPassword().equals(
                                request.getConfirmarPassword())) {

                        throw new IllegalArgumentException(
                                        "Las contraseñas no coinciden.");
                }

                // =================================================
                // GENERAR HASH DE LA NUEVA CONTRASEÑA
                // =================================================

                String passwordCodificada = passwordEncoder.encode(request.getPassword());

                // =================================================
                // ACTUALIZAR CONTRASEÑA
                // =================================================

                usuario.setPassword(passwordCodificada);

                // =================================================
                // GUARDAR EN BASE DE DATOS
                // =================================================

                return usuarioRepository.save(usuario);
        }
}
