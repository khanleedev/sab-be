package org.project.social_account_business.service.auth;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.LoginResponse;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.InvalidTokenException;
import org.project.social_account_business.form.auth.LoginForm;
import org.project.social_account_business.form.auth.TokenPair;
import org.project.social_account_business.form.otp.ForgetPasswordPayload;
import org.project.social_account_business.model.Account;
import org.project.social_account_business.model.TokenType;
import org.project.social_account_business.service.AsyncService;
import org.project.social_account_business.service.OTPService;
import org.project.social_account_business.service.account.AccountService;
import org.project.social_account_business.service.email.EmailService;
import org.project.social_account_business.utils.AESUtils;
import org.project.social_account_business.utils.ConvertUtils;
import org.project.social_account_business.utils.CookieUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    final AuthenticationManager authenticationManager;
    final TokenService tokenService;
    final UserDetailsService userDetailsService;
    final AccountService accountService;
    final EmailService emailService;
    final AsyncService asyncService;
    final CookieUtil cookieUtil;
    final OTPService otpService;
    final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenService tokenService, UserDetailsService userDetailsService, AccountService accountService, EmailService emailService, AsyncService asyncService, CookieUtil cookieUtil, OTPService otpService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
        this.emailService = emailService;
        this.asyncService = asyncService;
        this.cookieUtil = cookieUtil;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginForm loginForm) {
        log.info("Logging in");

        Account account = accountService.findAccountByUsernameAndPassword(loginForm.getEmail(), loginForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getEmail(), loginForm.getPassword()));

        HttpHeaders responseHeaders = new HttpHeaders();

        TokenPair tokenPair = tokenService.generateTokenPair(account);
        addRefreshTokenCookie(responseHeaders, tokenPair.getRefreshToken());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String message = emailService.getLoginEmailTemplate(account.getUsername(), String.valueOf(new Date()));
        asyncService.sendEmail(account.getEmail(), "Login confirmation", message, true);

        return new LoginResponse(tokenPair.getAccessToken(), responseHeaders, account.getKind());
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        log.info("Refreshing token");

        tokenService.validateToken(refreshToken, TokenType.REFRESH);
        tokenService.invalidateToken(refreshToken);

        Account account = tokenService.getAccountByToken(refreshToken);
        TokenPair tokenPair = tokenService.generateTokenPair(account);

        HttpHeaders responseHeaders = new HttpHeaders();
        addRefreshTokenCookie(responseHeaders, tokenPair.getRefreshToken());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
        return new LoginResponse(tokenPair.getAccessToken(), responseHeaders, account.getKind());
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if (StringUtils.isBlank(accessToken) || !accessToken.startsWith("Bearer ")) {
            throw new InvalidTokenException("[AuthService] Invalid authorization header");
        }
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidTokenException("[AuthService] Invalid refresh token");
        }
        log.info("Logging out");
        SecurityContextHolder.clearContext();
        String token = accessToken.substring(7);
        if (tokenService.validateToken(token, TokenType.ACCESS) && tokenService.validateToken(refreshToken, TokenType.REFRESH)) {
            tokenService.invalidateToken(token);
            tokenService.invalidateToken(refreshToken);
        }
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, String token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.refreshTokenCookie(token).toString());
    }

    private void deleteRefreshTokenCookie(HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshTokenCookie().toString());
    }

    @Override
    @Transactional
    public void confirmResetPassword(ForgetPasswordPayload payload) {
        String decrypted = AESUtils.decrypt(payload.getIdHash(), true);
        String[] parts = decrypted.split(";");
        Long id = ConvertUtils.convertStringToLong(parts[0]);
        String otp = parts[1];
        Account account = accountService.findById(id);
        if (id <= 0) {
            throw new BadRequestException("[AccountService] ❌ Invalid ID!", ErrorCode.ACCOUNT_ERROR_WRONG_HASH_RESET_PASS);
        }
        if (account.getAttemptCode() >= BetaConstant.MAX_ATTEMPT_FORGET_PWD ) {
            throw new BadRequestException("[AccountService] ❌ OTP attempt limit reached!", ErrorCode.OTP_ATTEMPT_LIMIT);
        }
        if (otpService.verifyEmailOTP(account.getEmail(), otp)) {
            account.setPassword(passwordEncoder.encode(payload.getNewPassword()));
            account.setResetPwdTime(null);
            account.setAttemptCode(0);
            accountService.save(account);
            log.info("[AccountService] ✅ Password reset successfully!");
        } else {
            account.setAttemptCode(account.getAttemptCode() + 1);
            accountService.save(account);
            throw new BadRequestException("[AccountService] ❌ Invalid OTP!", ErrorCode.INVALID_OTP);
        }
    }
    @Override
    @Transactional
    public ForgetPasswordPayload sendEmailOtpResetPassword(String email) {
        val account = accountService.findAccountByEmail(email);
        val otp = otpService.sendEmailVerificationPasswordResetOTP(email);
        account.setResetPwdTime(new Date());
        account.setAttemptCode(0);
        accountService.save(account);
        val forgetPasswordPayload = new ForgetPasswordPayload();
        String hash = AESUtils.encrypt(account.getId() + ";" + otp, true);
        forgetPasswordPayload.setIdHash(hash);
        log.info("[AccountService] ✅ OTP sent to email: " + email);
        return forgetPasswordPayload;
    }
}
