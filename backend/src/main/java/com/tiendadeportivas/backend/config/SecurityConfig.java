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

@Configuration
public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;

        public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
                this.customUserDetailsService = customUserDetailsService;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        PasswordEncoder passwordEncoder) {

                DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);

                provider.setPasswordEncoder(passwordEncoder);

                return new ProviderManager(provider);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {

                http
                                .cors(cors -> {
                                })

                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(
                                                                CookieCsrfTokenRepository.withHttpOnlyFalse())

                                                // =============================================
                                                // WEBHOOK DE STRIPE
                                                // ---------------------------------------------
                                                // Stripe llama directamente a este endpoint.
                                                // No utiliza nuestra sesión ni nuestro token
                                                // CSRF.
                                                //
                                                // La seguridad del webhook NO dependerá de CSRF:
                                                // verificaremos la firma criptográfica enviada
                                                // por Stripe.
                                                // =============================================

                                                .ignoringRequestMatchers(
                                                                "/api/stripe/webhook"))

                                .authorizeHttpRequests(auth -> auth

                                                // =================================================
                                                // GESTIÓN DE PEDIDOS
                                                // -------------------------------------------------
                                                // TRABAJADOR, JEFE y ADMIN pueden consultar
                                                // pedidos y modificar su estado.
                                                // =================================================

                                                .requestMatchers("/admin/pedidos/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                // =================================================
                                                // GESTIÓN DE PRODUCTOS
                                                // -------------------------------------------------
                                                // TRABAJADOR, JEFE y ADMIN pueden consultar,
                                                // crear y modificar productos.
                                                // =================================================

                                                .requestMatchers("/admin/productos/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                // =================================================
                                                // GESTIÓN DE EMPLEADOS
                                                // -------------------------------------------------
                                                // Solamente JEFE y ADMIN pueden acceder
                                                // a la gestión del personal.
                                                // =================================================

                                                .requestMatchers("/admin/usuarios/**")
                                                .hasAnyRole(
                                                                "JEFE",
                                                                "ADMIN")

                                                // =================================================
                                                // SEGURIDAD GENERAL DEL ÁREA ADMIN
                                                // -------------------------------------------------
                                                // Cualquier futuro endpoint /admin que olvidemos
                                                // configurar explícitamente seguirá necesitando
                                                // pertenecer al personal.
                                                // =================================================

                                                .requestMatchers("/admin/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                // =================================================
                                                // ÁREA PRIVADA DEL CLIENTE
                                                // -------------------------------------------------
                                                // Todos los endpoints relacionados con el perfil
                                                // personal del cliente requieren:
                                                // • Sesión iniciada.
                                                // • Rol CLIENTE.
                                                // =================================================

                                                .requestMatchers("/cliente/**")
                                                .hasRole("CLIENTE")

                                                // =================================================
                                                // CARRITO DEL CLIENTE
                                                // -------------------------------------------------
                                                // El carrito solo puede utilizarse si existe una
                                                // sesión iniciada con rol CLIENTE.
                                                //
                                                // Esto protege:
                                                // GET /carrito
                                                // POST /carrito
                                                // DELETE /carrito
                                                // DELETE /carrito/todo
                                                // =================================================

                                                .requestMatchers("/carrito/**", "/carrito")
                                                .hasRole("CLIENTE")

                                                // =================================================
                                                // REALIZACIÓN DE PEDIDOS
                                                // -------------------------------------------------
                                                // Solamente un cliente autenticado puede consultar
                                                // el resumen de compra o confirmar un pedido.
                                                // =================================================

                                                .requestMatchers("/pedido/**", "/pedido")
                                                .hasRole("CLIENTE")

                                                
                                                // Solamente un CLIENTE autenticado puede iniciar
                                                // el pago de su carrito mediante Stripe.
                                                // =================================================

                                                // =================================================
                                                // STRIPE - WEBHOOK
                                                // -------------------------------------------------
                                                // Stripe necesita acceder a este endpoint sin
                                                // iniciar sesión en nuestra tienda.
                                                //
                                                // Su autenticidad se comprobará mediante la firma
                                                // Stripe-Signature.
                                                // =================================================

                                                .requestMatchers("/api/stripe/webhook")
                                                .permitAll()

                                                // =================================================
                                                // STRIPE - CHECKOUT DEL CLIENTE
                                                // -------------------------------------------------
                                                // Solo un CLIENTE autenticado puede iniciar
                                                // una sesión de pago.
                                                // =================================================

                                                .requestMatchers("/api/stripe/checkout/**")
                                                .hasRole("CLIENTE")

                                                // =================================================
                                                // RESTO DE ENDPOINTS
                                                // -------------------------------------------------
                                                // Tienda, login, registro, catálogo, etc.
                                                // continúan funcionando con normalidad.
                                                // =================================================

                                                .anyRequest()
                                                .permitAll());

                return http.build();
        }

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
