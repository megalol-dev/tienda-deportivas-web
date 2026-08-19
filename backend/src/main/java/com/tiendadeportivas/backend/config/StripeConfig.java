// Configura Stripe con la clave del entorno.
package com.tiendadeportivas.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String secretKey;

    // Establece la clave secreta de Stripe.
    @PostConstruct
    public void configurarStripe() {

        Stripe.apiKey = secretKey;
    }
}
