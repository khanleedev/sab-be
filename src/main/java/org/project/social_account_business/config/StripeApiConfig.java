package org.project.social_account_business.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeApiConfig {
    @Value("${stripe.api.secret.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        // Set the API key for Stripe
        com.stripe.Stripe.apiKey = apiKey;
    }
}
