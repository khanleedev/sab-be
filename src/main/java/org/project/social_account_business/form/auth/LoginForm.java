package org.project.social_account_business.form.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    @Schema(name = "username", description = "User Email", example = "abc123@gmail.com")
    @NotEmpty(message = "Username is required")
    private String email;
    @Schema(name = "password", description = "User Password", example = "password")
    @NotEmpty(message = "Password is required")
    private String password;
//
//    public LoginForm() {
//    }
//
//    public LoginForm(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
}
