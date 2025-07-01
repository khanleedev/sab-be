package org.project.social_account_business.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paypal")
@Getter
@Setter
public class  PaypalApiRestTemplateConfig {
    @Value("${sandbox.url}")
    private String url;
    @Value("${sandbox.client.id}")
    private String clientId;
    @Value("${sandbox.client.secret-key}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;

    public PayPalHttpClient payPalHttpClient() {
        PayPalEnvironment environment = mode.equals("sandbox") ?
                new PayPalEnvironment.Sandbox(clientId, clientSecret) :
                new PayPalEnvironment.Live(clientId, clientSecret);
        return new PayPalHttpClient(environment);
    }
}
