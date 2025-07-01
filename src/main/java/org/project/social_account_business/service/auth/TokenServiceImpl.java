package org.project.social_account_business.service.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.exception.InvalidTokenException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.auth.TokenPair;
import org.project.social_account_business.model.Account;
import org.project.social_account_business.model.TokenInfo;
import org.project.social_account_business.model.TokenType;
import org.project.social_account_business.repository.AccountRepository;
import org.project.social_account_business.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.secret-key}")
    private String secret;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;

    public TokenServiceImpl(TokenRepository tokenRepository, AccountRepository accountRepository) {
        this.tokenRepository = tokenRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public TokenPair generateTokenPair(Account account) {
        String accessToken = generateAccessToken(account);
        String refreshToken = generateRefreshToken(account);
        saveToken(accessToken, TokenType.ACCESS, account);
        saveToken(refreshToken, TokenType.REFRESH, account);
        return new TokenPair(accessToken, refreshToken);
    }

    private String generateAccessToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getEmail())
                .claim("id", account.getId())
                .claim("role", account.getAuthorities())
                .claim("type", TokenType.ACCESS.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    public String generateApiKeyToken(Account account) {
//        return Jwts.builder()
//                .setSubject("Services@skmedia24h.com")
//                .setIssuer("sab-authentication-system")
//                .setAudience("sepay-webhook")
//                .claim("type", "API_KEY")
//                .setIssuedAt(new Date())
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateRefreshToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getEmail())
                .claim("id", account.getId())
                .claim("type", TokenType.REFRESH.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    @Transactional
    public TokenPair refreshTokens(String refreshToken) {
        if (!validateToken(refreshToken, TokenType.REFRESH)) {
            throw new InvalidTokenException("[TokenService] Invalid refresh token");
        }
        if (isTokenExpired(refreshToken)) {
            throw new InvalidTokenException("[TokenService] Refresh token has expired");
        }
        String email = getUsernameFromToken(refreshToken);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new NotFoundException("[TokenService] ❌ Account not found", ErrorCode.ACCOUNT_NOT_FOUND));
        invalidateToken(refreshToken);
        return generateTokenPair(account);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token)
            throws InvalidTokenException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    @Transactional
    public void saveToken(String token, TokenType tokenType, Account account) {
        TokenInfo tokenInfoObj = new TokenInfo(
                token,
                getExpirationDateFromToken(token),
                account,
                tokenType);
        tokenInfoObj.setCreatedBy("AuthenticationSystem");

        tokenRepository.save(tokenInfoObj);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
            throws InvalidTokenException {
        val claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaimFromToken(token, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            log.info("Token expired for user: {}", ex.getClaims().getSubject());
            return true;
        } catch (Exception ex) {
            log.warn("Token validation error: {}", ex.getMessage());
            return true;
        }
    }

    @Override
    public boolean validateToken(String token, TokenType expectedType) {
        try {
            TokenInfo storedTokenInfo = tokenRepository.findByToken(token);
            if (storedTokenInfo == null || !storedTokenInfo.getType().equals(expectedType)) {
                return false;
            }

            Claims claims = getAllClaimsFromToken(token);
            String tokenType = claims.get("type", String.class);
            return expectedType.name().equals(tokenType);
        } catch (InvalidTokenException ex) {
            log.warn("Token validation failed: {}", ex.getMessage());
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Token expired for user: {}", e.getClaims().getSubject());
            throw new InvalidTokenException("Token has expired. Please refresh or login again");
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("Unsupported token format");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Invalid token structure");
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid tokenIllegalArgumentException");
        } catch (Exception e) {
            log.error("Error parsing token: {}", e.getMessage());
            throw new InvalidTokenException("Error parsing token");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void validateToken(String token) {
        if (tokenRepository.findByToken(token) == null) {
            throw new InvalidTokenException("[TokenService] ❌ Token not found");
        }
    }

    @Override
    @Transactional
    public void invalidateToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void invalidateAllRefreshTokens(String email) {
        tokenRepository.deleteAllByAccountEmailAndType(email, TokenType.REFRESH);
    }

    @Override
    public Account getAccountByToken(String token) {
        Account account = new Account();
        Claims claims = getAllClaimsFromToken(token);
        log.info("Claims: {}", claims);
        String subject = (String) claims.get(Claims.SUBJECT);
        account.setEmail(subject);
        account.setId(Long.parseLong(claims.get("id").toString()));
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("[TokenService] ❌ User not found with email: %s", email)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .authorities(account.getAuthorities())
                .build();
    }
}