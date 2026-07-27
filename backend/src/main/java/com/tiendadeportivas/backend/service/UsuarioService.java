package com.tiendadeportivas.backend.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tiendadeportivas.backend.model.LoginRequest;
import com.tiendadeportivas.backend.model.RegistroUsuarioRequest;
import com.tiendadeportivas.backend.model.RolUsuario;
import com.tiendadeportivas.backend.model.Usuario;
import com.tiendadeportivas.backend.model.UsuarioRespuesta;
import com.tiendadeportivas.backend.repository.UsuarioRepository;

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
}
