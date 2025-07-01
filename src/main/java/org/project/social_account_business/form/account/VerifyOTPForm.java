package org.project.social_account_business.form.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOTPForm {
    @NotEmpty(message = "OTP can not be empty!")
    @Schema(name = "otp", required = true, example = "123456")
    private String otp;

    @NotEmpty(message = "idHash can not be empty!")
    @Schema(name = "idHash", required = true)
    private String idHash;

    @NotEmpty(message = "newPassword can not be empty!")
    @Size(min = 6, message = "newPassword minimum 6 characters!")
    @Schema(name = "newPassword", required = true, example = "newPassword")
    private String newPassword;
}
