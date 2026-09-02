// Configura autenticación, permisos, CSRF y CORS.
package com.tiendadeportivas.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.tiendadeportivas.backend.security.CustomUserDetailsService;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;

        // Crea una instancia de SecurityConfig.
        public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
                this.customUserDetailsService = customUserDetailsService;
        }

        // Crea el codificador de contraseñas.
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // Registra las sesiones activas de los usuarios.
        @Bean
        public SessionRegistry sessionRegistry() {
                return new SessionRegistryImpl();
        }

        // Mantiene actualizado el registro cuando una sesión termina.
        @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
                return new HttpSessionEventPublisher();
        }

        // Crea el gestor de autenticación.
        @Bean
        public AuthenticationManager authenticationManager(
                        PasswordEncoder passwordEncoder) {

                DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);

                provider.setPasswordEncoder(passwordEncoder);

                return new ProviderManager(provider);
        }

        // Define la seguridad de las rutas HTTP.
        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http,
                        SessionRegistry sessionRegistry)
                        throws Exception {

                http
                                .cors(cors -> {
                                })

                                // Conserva el registro necesario para revocar sesiones activas.
                                .sessionManagement(session -> session
                                                .maximumSessions(-1)
                                                .sessionRegistry(sessionRegistry))

                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(
                                                                CookieCsrfTokenRepository.withHttpOnlyFalse())

                                                // Stripe autentica el webhook mediante su propia firma.
                                                .ignoringRequestMatchers(
                                                                "/api/stripe/webhook"))

                                .authorizeHttpRequests(auth -> auth

                                                // Rutas públicas de autenticación.
                                                .requestMatchers(
                                                                "/auth/login",
                                                                "/auth/registro",
                                                                "/auth/csrf")
                                                .permitAll()

                                                // Catálogo público.
                                                .requestMatchers("/productos")
                                                .permitAll()

                                                // Endpoint de comprobación del servidor.
                                                .requestMatchers("/api/saludo")
                                                .permitAll()

                                                // Permite el dispatch interno de errores de Spring.
                                                .requestMatchers("/error")
                                                .permitAll()

                                                // Webhook público de Stripe.
                                                .requestMatchers("/api/stripe/webhook")
                                                .permitAll()

                                                // Operaciones que requieren cualquier sesión autenticada.
                                                .requestMatchers(
                                                                "/auth/me",
                                                                "/auth/logout")
                                                .authenticated()

                                                // Gestión de empleados.
                                                .requestMatchers("/admin/usuarios/**")
                                                .hasAnyRole(
                                                                "JEFE",
                                                                "ADMIN")

                                                // Gestión de pedidos.
                                                .requestMatchers("/admin/pedidos/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                // Gestión de productos.
                                                .requestMatchers("/admin/productos/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                // Resto del área de personal.
                                                .requestMatchers("/admin/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                // Perfil del cliente.
                                                .requestMatchers("/cliente/**")
                                                .hasRole("CLIENTE")

                                                // Carrito del cliente.
                                                .requestMatchers(
                                                                "/carrito",
                                                                "/carrito/**")
                                                .hasRole("CLIENTE")

                                                // Pedidos del cliente.
                                                .requestMatchers(
                                                                "/pedido",
                                                                "/pedido/**")
                                                .hasRole("CLIENTE")

                                                // Checkout de Stripe.
                                                .requestMatchers("/api/stripe/checkout/pedido")
                                                .hasRole("CLIENTE")

                                                // Todo lo no declarado explícitamente queda bloqueado.
                                                .anyRequest()
                                                .denyAll());

                return http.build();
        }

        // Permite las peticiones del frontend local.
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(List.of(
                                "http://127.0.0.1:5500",
                                "http://localhost:5500"));

                configuration.setAllowedMethods(List.of(
                                "GET",
                                "POST",
                                "PUT",
                                "PATCH",
                                "DELETE",
                                "OPTIONS"));

                configuration.setAllowedHeaders(List.of("*"));

                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}
