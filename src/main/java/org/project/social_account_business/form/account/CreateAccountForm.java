package org.project.social_account_business.form.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.project.social_account_business.validation.NumberField;

@Getter
@Setter
@Data
public class CreateAccountForm {
    @Schema(description = "Username", example = "username")
    @NotEmpty(message = "Username is required")
    private String username;
    @Schema(description = "Password", example = "password")
    @NotEmpty(message = "Password is required")
    private String password;
    @Schema(description = "Email", example = "email")
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @Schema(description = "Phone number", example = "phone number")
    @NumberField(message = "Phone number is invalid")
    private String phoneNo;
}
