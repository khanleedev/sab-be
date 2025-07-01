package org.project.social_account_business.config;

import jakarta.servlet.http.HttpServletResponse;
import org.project.social_account_business.jwt.JwtAuthenticationEntryPoint;
import org.project.social_account_business.jwt.JwtAuthenticationFilter;
import org.project.social_account_business.service.auth.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    final TokenService tokenService;
    final JwtAuthenticationFilter jwtAuthenticationFilter;
    final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Value("${jwt.cookie.access-token.name}")
    private String accessTokenCookieName;
    @Value("${jwt.cookie.refresh-token.name}")
    private String refreshTokenCookieName;

    public SecurityConfig(TokenService tokenService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.tokenService = tokenService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(tokenService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/accounts/users/register", "/v1/auth/login", "/v1/auth/reset-password",
                                "/v1/auth/reset-password/confirm", "/v1/payment-transactions/webhooks/sepay-payment",
                                "/v1/tickets", "/v1/reports/public",
                                "/v1/auth/refresh", "/v1/auth/logout",
                                "/v1/otps/send-otp-email", "/v1/otps/verify-otp", "/swagger-ui.html",
                                "/v3/api-docs/**", "/swagger-ui/**", "/actuator/**", "/favicon.ico",
                                "/manage/**", "/error/**", "/v1/reports/resolved", "/ws/**", "/v1/paypal/webhook").permitAll()
                        .requestMatchers("/v1/accounts/get-me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/tickets/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/ticket-products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/transactions/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/payment-transactions/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/orders/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/reports/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
//                .formLogin(form -> form.loginPage("/login"))
                .logout(logout -> logout
                        .logoutUrl("/v1/auth/logout")
                        .logoutSuccessUrl("/v1/auth/login")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies(accessTokenCookieName, refreshTokenCookieName))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/error",
                "/favicon.ico",
                "/manage/**",
                "/actuator/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:7070", "http://localhost:5173", "http://localhost:5174",
                "https://editor.swagger.io", "103.255.238.9", "https://sab-fe-fw5i.vercel.app/", "https://www.skmedia24h.com"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Accept", "Origin", "Content-Type", "Depth", "User-Agent", "If-Modified-Since,",
                "Cache-Control", "Authorization", "X-Req", "X-File-Size", "X-Requested-With",
                "X-File-Name", "Content-Disposition"));
        configuration.setExposedHeaders(Arrays.asList(
                "Origin", "X-Requested-With", "Content-Type", "Accept",
                "Authorization", "Content-Disposition"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
