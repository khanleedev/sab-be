package org.project.social_account_business.form.otp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgetPasswordPayload {
    @NotEmpty(message = "OTP can not be empty!")
    @Schema(name = "otp", required = true)
    private String otp;

    @NotEmpty(message = "idHash can not be empty!")
    @Schema(name = "idHash", required = true)
    private String idHash;

    @NotEmpty(message = "newPassword can not be empty!")
    @Size(min = 6, message = "newPassword minimum 6 characters!")
    @Schema(name = "newPassword", required = true)
    private String newPassword;
}
