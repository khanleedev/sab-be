package org.project.social_account_business.service.auth;

import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.LoginResponse;
import org.project.social_account_business.form.auth.LoginForm;
import org.project.social_account_business.form.otp.ForgetPasswordPayload;
import org.springframework.http.HttpHeaders;

public interface AuthService {
   /**
    * Authenticate a user and generate a login response containing access and refresh tokens.
    *
    * @param loginForm the form containing login credentials (e.g., username and password)
    * @return the login response with tokens and user details
    */
   ApiResponse<LoginResponse> login(LoginForm loginForm);

   /**
    * Refresh the access token using a valid refresh token.
    *
    * @param refreshToken the refresh token to generate a new access token
    * @return the login response with a new access token and potentially a new refresh token
    */
   ApiResponse<LoginResponse> refreshToken(String refreshToken);

   /**
    * Log out a user by invalidating the provided access and refresh tokens.
    *
    * @param accessToken the access token to invalidate
    * @param refreshToken the refresh token to invalidate
    */
   void logout(String accessToken, String refreshToken);

   /**
    * Confirm and process a password reset request using the provided payload.
    *
    * @param payload the payload containing the reset password details (e.g., OTP and new password)
    */
   void confirmResetPassword(ForgetPasswordPayload payload);

   /**
    * Send an email with an OTP for resetting the password.
    *
    * @param email the email address to send the OTP to
    * @return the payload containing the OTP and related information
    */
   ForgetPasswordPayload sendEmailOtpResetPassword(String email);
}
