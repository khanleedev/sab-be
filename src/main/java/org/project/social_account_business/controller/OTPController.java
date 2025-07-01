package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.otp.OTPRequestForm;
import org.project.social_account_business.form.otp.VerifyOTPForm;
import org.project.social_account_business.service.OTPService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/v1/otps")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OTPController {
    private final OTPService otpService;

    public OTPController(OTPService otpService) {
        this.otpService = otpService;
    }

    @PostMapping(value = "/send-otp-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<T>> sendOTPEmail(@Valid @RequestBody OTPRequestForm otpRequestForm, BindingResult bindingResult) {
        log.info("Sending OTP email");
        if (bindingResult.hasErrors()) {
            throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        otpService.sendEmailVerificationOTPForRegister(otpRequestForm.getEmail(), otpRequestForm.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "OTP sent successfully"));
    }

    @PostMapping(value = "/verify-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<T>> verifyOTP(@Valid @RequestBody VerifyOTPForm verifyOTPForm, BindingResult bindingResult) {
        log.info("Verifying OTP");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }
        otpService.verifyEmailOTP(verifyOTPForm.getEmail(), verifyOTPForm.getOtp());
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "OTP verified successfully"));
    }
}
