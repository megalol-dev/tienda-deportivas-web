package com.tiendadeportivas.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.core.AuthenticationException;

import com.tiendadeportivas.backend.model.LoginRequest;
import com.tiendadeportivas.backend.model.RegistroUsuarioRequest;
import com.tiendadeportivas.backend.model.Usuario;
import com.tiendadeportivas.backend.model.UsuarioRespuesta;
import com.tiendadeportivas.backend.repository.UsuarioRepository;
import com.tiendadeportivas.backend.service.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
public class AuthController {

        private final UsuarioService usuarioService;
        private final AuthenticationManager authenticationManager;
        private final UsuarioRepository usuarioRepository;

        public AuthController(
                        UsuarioService usuarioService,
                        AuthenticationManager authenticationManager,
                        UsuarioRepository usuarioRepository) {

                this.usuarioService = usuarioService;
                this.authenticationManager = authenticationManager;
                this.usuarioRepository = usuarioRepository;
        }

        @PostMapping("/registro")
        @ResponseStatus(HttpStatus.CREATED)
        public UsuarioRespuesta registrar(
                        @Valid @RequestBody RegistroUsuarioRequest request) {

                Usuario usuario = usuarioService.registrarCliente(request);

                return new UsuarioRespuesta(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol(),
                                usuario.getFechaAlta());
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(
                        @Valid @RequestBody LoginRequest request,
                        HttpServletRequest httpRequest) {

                try {

                        // Spring Security comprueba email + contraseña
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getEmail(),
                                                        request.getPassword()));

                        // Creamos el SecurityContext
                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                        securityContext.setAuthentication(authentication);

                        SecurityContextHolder.setContext(securityContext);

                        // Creamos/obtenemos la sesión HTTP
                        HttpSession session = httpRequest.getSession(true);

                        // Guardamos el SecurityContext en la sesión
                        session.setAttribute(
                                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                                        securityContext);

                        // Recuperamos nuestro usuario
                        Usuario usuario = usuarioRepository
                                        .findByEmail(authentication.getName())
                                        .orElseThrow(() -> new SecurityException(
                                                        "Usuario autenticado no encontrado"));

                        UsuarioRespuesta respuesta = new UsuarioRespuesta(
                                        usuario.getId(),
                                        usuario.getNombre(),
                                        usuario.getEmail(),
                                        usuario.getRol(),
                                        usuario.getFechaAlta());

                        return ResponseEntity.ok(respuesta);

                } catch (AuthenticationException e) {

                        return ResponseEntity
                                        .status(HttpStatus.UNAUTHORIZED)
                                        .body(Map.of(
                                                        "message",
                                                        "Email o contraseña incorrectos."));
                }
        }

        @GetMapping("/me")
        public UsuarioRespuesta obtenerUsuarioActual(Authentication authentication) {

                if (authentication == null || !authentication.isAuthenticated()) {
                        throw new SecurityException("No hay ningún usuario autenticado");
                }

                Usuario usuario = usuarioRepository
                                .findByEmail(authentication.getName())
                                .orElseThrow(() -> new SecurityException(
                                                "Usuario autenticado no encontrado"));

                return new UsuarioRespuesta(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getEmail(),
                                usuario.getRol(),
                                usuario.getFechaAlta());
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(
                        HttpServletRequest request,
                        HttpServletResponse response) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication != null) {
                        new SecurityContextLogoutHandler()
                                        .logout(request, response, authentication);
                }

                return ResponseEntity.noContent().build();
        }

        @GetMapping("/csrf")
        public CsrfToken csrf(CsrfToken token) {
                return token;
        }

}
