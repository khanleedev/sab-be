package org.project.social_account_business.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component("cookieUtil")
@Getter
@Slf4j
public class CookieUtil {
    @Value("${jwt.cookie.access-token.name}")
    private String accessTokenCookieName;

    @Value("${jwt.cookie.refresh-token.name}")
    private String refreshTokenCookieName;

    @Value("${jwt.cookie.domain}")
    private String cookieDomain;

    @Value("${jwt.cookie.secure}")
    private boolean cookieSecure;
    @Value("${jwt.access-token.expiration}")
    private Long accessTokenDuration;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenDuration;
    public ResponseCookie accessTokenCookie(String token) {
        return ResponseCookie.from(accessTokenCookieName, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .domain(cookieDomain)
                .path("/")
                .maxAge(accessTokenDuration)
                .build();
    }

    public ResponseCookie refreshTokenCookie(String token) {
        return ResponseCookie.from(refreshTokenCookieName, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .domain(cookieDomain)
                .path("/")
                .maxAge(refreshTokenDuration)
                .build();
    }

    public ResponseCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(accessTokenCookieName, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .domain(cookieDomain)
                .path("/")
                .maxAge(0)
                .build();
    }

    public ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenCookieName, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .domain(cookieDomain)
                .path("/")
                .maxAge(0)
                .build();
    }

}