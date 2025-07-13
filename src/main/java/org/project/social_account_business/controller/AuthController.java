package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.LoginResponse;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.auth.LoginForm;
import org.project.social_account_business.form.otp.ForgetPasswordPayload;
import org.project.social_account_business.form.otp.RequestForgetPasswordForm;
import org.project.social_account_business.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/v1/auth")
@CrossOrigin(origins = "https://www.skmedia24h.com", maxAge = 3600)
@Slf4j
public class AuthController {

    final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginForm loginForm, BindingResult bindingResult) {
        log.info("Login attempt for email: {}", loginForm.getUsername());
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Invalid login form", null));
        }
        LoginResponse loginResponse = authService.login(loginForm);
        return ResponseEntity.ok().headers(loginResponse.getHeaders()).body(new ApiResponse<>(HttpStatus.OK, String.valueOf(loginResponse.getKind()), loginResponse));
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestParam("refresh_token") String refreshToken) {
        log.info("Refreshing token");
        LoginResponse loginResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok().headers(loginResponse.getHeaders()).body(new ApiResponse<>(HttpStatus.OK, "Token refreshed successfully", loginResponse));
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String header,
                                                      @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        log.info("Logging out");
        authService.logout(header, refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Logged out successfully"));
    }

    @PostMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<ForgetPasswordPayload>> resetPassword(@Valid @RequestBody RequestForgetPasswordForm form, BindingResult bindingResult) {
        log.info("Resetting password");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Request password reset successfully", authService.sendEmailOtpResetPassword(form.getEmail())));
    }

    @PostMapping(value = "/reset-password/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<String>> confirmResetPassword(@Valid @RequestBody ForgetPasswordPayload passwordPayload, BindingResult bindingResult) {
        log.info("Confirming password reset");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }
        authService.confirmResetPassword(passwordPayload);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Password reset successfully"));
    }
}
