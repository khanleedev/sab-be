package org.project.social_account_business.form.otp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RequestForgetPasswordForm {
    @Email
    @Schema(name = "email", required = true)
    @NotEmpty(message = "Email can not be empty!")
    private String email;
}
