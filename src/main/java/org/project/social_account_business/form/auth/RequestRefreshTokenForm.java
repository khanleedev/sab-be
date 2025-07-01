package org.project.social_account_business.form.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RequestRefreshTokenForm {
    @Schema(description = "Refresh token", required = true)
    @NotEmpty(message = "Refresh token is required")
    private String refreshToken;
}
