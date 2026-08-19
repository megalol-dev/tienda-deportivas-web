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
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {

                http
                                .cors(cors -> {
                                })

                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(
                                                                CookieCsrfTokenRepository.withHttpOnlyFalse())

                                                .ignoringRequestMatchers(
                                                                "/api/stripe/webhook"))

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers("/admin/pedidos/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                .requestMatchers("/admin/productos/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                .requestMatchers("/admin/usuarios/**")
                                                .hasAnyRole(
                                                                "JEFE",
                                                                "ADMIN")

                                                .requestMatchers("/admin/**")
                                                .hasAnyRole(
                                                                "TRABAJADOR",
                                                                "JEFE",
                                                                "ADMIN")

                                                .requestMatchers("/cliente/**")
                                                .hasRole("CLIENTE")

                                                .requestMatchers("/carrito/**", "/carrito")
                                                .hasRole("CLIENTE")

                                                .requestMatchers("/pedido/**", "/pedido")
                                                .hasRole("CLIENTE")

                                                .requestMatchers("/api/stripe/webhook")
                                                .permitAll()

                                                .requestMatchers("/api/stripe/checkout/**")
                                                .hasRole("CLIENTE")

                                                .anyRequest()
                                                .permitAll());

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
