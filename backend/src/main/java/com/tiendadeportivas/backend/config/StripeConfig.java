package com.tiendadeportivas.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    // Spring obtiene este valor de:
    // stripe.secret-key=${STRIPE_SECRET_KEY}
    // que tenemos configurado en application.properties.
    @Value("${stripe.secret-key}")
    private String secretKey;

    // Se ejecuta automáticamente cuando Spring crea esta configuración.
    @PostConstruct
    public void configurarStripe() {

        // Le entregamos a la librería de Stripe nuestra clave secreta.
        Stripe.apiKey = secretKey;
    }
}