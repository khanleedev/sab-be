package org.project.social_account_business.config;

import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.component.CustomHandShakeHandler;
import org.project.social_account_business.component.JwtHandShakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandShakeInterceptor jwtHandShakeInterceptor;

    public WebSocketConfig(JwtHandShakeInterceptor jwtHandShakeInterceptor) {
        this.jwtHandShakeInterceptor = jwtHandShakeInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Registering STOMP endpoint: /ws");
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .setHandshakeHandler(new CustomHandShakeHandler())
                .addInterceptors(jwtHandShakeInterceptor)
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("Configuring message broker");
        registry.enableSimpleBroker("/account/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/account");
    }
}