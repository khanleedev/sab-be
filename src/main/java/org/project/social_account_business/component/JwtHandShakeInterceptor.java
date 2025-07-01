package org.project.social_account_business.component;

import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.service.auth.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
public class JwtHandShakeInterceptor implements HandshakeInterceptor {
    private final TokenService tokenService;

    public JwtHandShakeInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        try{
            if (request instanceof ServletServerHttpRequest servletRequest) {
                String token = servletRequest.getServletRequest().getHeader("Authorization");
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    String email = tokenService.getUsernameFromToken(token);
                    attributes.put("email", email);
                }
            }
            return true;
        }
        catch (Exception e) {
            log.warn("Invalid JWT during WebSocket handshake: {}", e.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED); // Unauthorized
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
