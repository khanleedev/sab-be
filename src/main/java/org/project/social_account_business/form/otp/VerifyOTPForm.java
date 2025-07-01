package org.project.social_account_business.form.otp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VerifyOTPForm {
    @Email
    @NotEmpty(message = "Email is required")
    @Schema(description = "Email", example = "email")
    private String email;
    @NotEmpty(message = "OTP is required")
    @Schema(description = "OTP", example = "123456")
    private String otp;
}
