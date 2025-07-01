package org.project.social_account_business.form.otp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OTPRequestForm {
    @NotEmpty
    @Schema(description = "Email", example = "email")
    private String email;
    @NotEmpty
    @Schema(description = "Username", example = "username")
    private String username;
}
