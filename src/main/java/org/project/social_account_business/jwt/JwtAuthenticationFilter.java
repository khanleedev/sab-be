package org.project.social_account_business.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.InvalidTokenException;
import org.project.social_account_business.service.auth.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    @Value("${jwt.sepay.secret-key}")
    private String sepaySecretKey;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing authentication for request: " + request.getRequestURI());
        if (request.getRequestURI().contains("/v1/auth") || request.getRequestURI().contains("/social-account-business-ws")
                || request.getRequestURI().contains("/v1/otps") || request.getRequestURI().contains("/v3/api-docs")
                || request.getRequestURI().contains("/v3/api-docs/swagger-config")
                || request.getRequestURI().contains("/swagger-ui")
                || request.getRequestURI().contains("/swagger-ui.html")) {
            log.info("Skipping authentication for auth endpoint");
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().contains("/webhooks/sepay-payment")) {
            String apiKeyHeader = request.getHeader("Authorization");
            log.info("Starting JWT authentication filter");
            if (apiKeyHeader != null && apiKeyHeader.startsWith("Apikey ")) {
                String apiKey = apiKeyHeader.substring(7);
                if (!apiKey.equals(sepaySecretKey)) {
                    log.warn("Invalid API key");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
                    return;
                }
            } else {
                log.warn("Missing or invalid API key");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid API key");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        String requestTokenHeader = request.getHeader("Authorization");
        log.info("Starting JWT authentication filter");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String token = requestTokenHeader.substring(7);
            String email;

            try {
                tokenService.validateToken(token);
                email = tokenService.getUsernameFromToken(token);
            } catch (InvalidTokenException e) {
                logger.warn("Invalid token: " + e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Continue processing the request (JWT Authentication or other filters)
        filterChain.doFilter(request, response);
    }
}