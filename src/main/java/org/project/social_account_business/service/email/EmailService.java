package org.project.social_account_business.service.email;

import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

public interface EmailService {
    /**
     * Send an email asynchronously.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param text the content of the email
     * @param html whether the email content is in HTML format
     * @return a CompletableFuture representing the asynchronous operation
     */
    @Async
    CompletableFuture<Void> sendEmail(String to, String subject, String text, boolean html);

    /**
     * Generate the email template for login notification.
     *
     * @param name the name of the recipient
     * @param loginTime the time of login
     * @return the login email template as a string
     */
    String getLoginEmailTemplate(String name, String loginTime);

    /**
     * Generate the email template for OTP login.
     *
     * @param name the name of the recipient
     * @param otp the one-time password
     * @return the OTP login email template as a string
     */
    String getOtpLoginEmailTemplate(String name, String otp);

    /**
     * Generate the email template for password reset OTP request.
     *
     * @param name the name of the recipient
     * @param otp the one-time password for resetting the password
     * @return the password reset OTP request email template as a string
     */
    String getPasswordResetOtpRequestTemplate(String name, String otp);

    /**
     * Generate the email template for successful password reset notification.
     *
     * @param name the name of the recipient
     * @param resetTime the time when the password was reset
     * @return the password reset success email template as a string
     */
    String getPasswordResetSuccessTemplate(String name, String resetTime);

    /**
     * Generate the email template for order completion notification.
     *
     * @param name the name of the recipient
     * @param transactionCode the transaction code
     * @param amountInCoin the amount in coin
     * @param time the time of transaction
     * @param transactionStatus the status of the transaction
     * @param ticketProductName the name of the ticket product
     * @param quantity the quantity of tickets purchased
     * @param ticketProductItemCode the item code of the ticket product
     * @return the order completion email template as a string
     */
    String getEmailOrderCompleteTemplate(String name, String transactionCode, Double amountInCoin, Date time, String transactionStatus, String ticketProductName, Integer quantity, String ticketProductItemCode);


    String getEmailApologizeForBalanceErrorTemplate(String name, String transactionCode, double amount, Date correctionTime);
}
