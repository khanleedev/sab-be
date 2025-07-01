package org.project.social_account_business.service.auth;

import io.jsonwebtoken.Claims;
import org.project.social_account_business.exception.InvalidTokenException;
import org.project.social_account_business.form.auth.TokenPair;
import org.project.social_account_business.model.Account;
import org.project.social_account_business.model.TokenType;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;
import java.util.function.Function;

public interface TokenService extends UserDetailsService {
    /**
     * Generate a pair of access and refresh tokens for the given account.
     *
     * @param account the account for which the tokens are generated
     * @return a pair of access and refresh tokens
     */
    public TokenPair generateTokenPair(Account account);

    /**
     * Refresh the access and refresh tokens using the provided refresh token.
     *
     * @param refreshToken the refresh token to generate new tokens
     * @return a new pair of access and refresh tokens
     */
    public TokenPair refreshTokens(String refreshToken);

    /**
     * Extract the username from the given token.
     *
     * @param token the token from which the username is extracted
     * @return the username contained in the token
     */
    public String getUsernameFromToken(String token);

    /**
     * Retrieve the expiration date of the given token.
     *
     * @param token the token to check for expiration
     * @return the expiration date of the token
     */
    public Date getExpirationDateFromToken(String token);

    /**
     * Extract a specific claim from the given token using a claims resolver function.
     *
     * @param token          the token from which the claim is extracted
     * @param claimsResolver the function to resolve the claim
     * @param <T>            the type of the claim
     * @return the extracted claim
     * @throws InvalidTokenException if the token is invalid
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
            throws InvalidTokenException;

    /**
     * Save a token with its type and associated account.
     *
     * @param token     the token to save
     * @param tokenType the type of the token (e.g., ACCESS, REFRESH)
     * @param account   the account associated with the token
     */
    public void saveToken(String token, TokenType tokenType, Account account);

    /**
     * Validate the given token to ensure it is not expired or tampered with.
     *
     * @param token the token to validate
     */
    public void validateToken(String token);

    /**
     * Check if the given token is expired.
     *
     * @param token the token to check
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token);

    /**
     * Validate the given token against a specific token type.
     *
     * @param token     the token to validate
     * @param tokenType the expected type of the token
     * @return true if the token is valid for the given type, false otherwise
     */
    public boolean validateToken(String token, TokenType tokenType);

    /**
     * Invalidate the given token, making it unusable.
     *
     * @param token the token to invalidate
     */
    public void invalidateToken(String token);

    /**
     * Invalidate all refresh tokens associated with the given email.
     *
     * @param email the email whose refresh tokens should be invalidated
     */
    public void invalidateAllRefreshTokens(String email);

    /**
     * Retrieve the account associated with the given token.
     *
     * @param token the token to use for retrieving the account
     * @return the account associated with the token
     */
    Account getAccountByToken(String token);
}
