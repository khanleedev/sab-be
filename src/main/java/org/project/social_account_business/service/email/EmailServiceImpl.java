package org.project.social_account_business.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.model.TicketProduct;
import org.project.social_account_business.model.TicketProductInfo;
import org.project.social_account_business.service.OTPService;
import org.project.social_account_business.service.currency.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${global-link.logo-base64}")
    private String logoBase64;

    @Override
    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text, boolean html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, html);
            helper.setFrom(fromEmail);
            mailSender.send(message);

            log.info("Sent email to {}", to);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public String getGeneralEmailTemplate(String name, String message, String subject) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<img src=\"" + logoBase64 + "\" alt=\"Global Link Logo\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "<h1 style=\"font-size: 1.8em; color: #1a237e; margin: 10px 0;\">Global Link</h1>"
                + "</div>"
                + "<div style=\"padding: 20px;\">"
                + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #333;\">" + message + "</p>"
                + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p><strong>GlobalLink Media, LLC</strong></p>"
                + "<p>131 Continental Dr, Suite 305, Newark, DE 19713, US</p>"
                + "<p>Contact Us:</p>"
                + "<p>WhatsApp: <a href=\"https://wa.me/84935857801\" style=\"color: #1a237e; text-decoration: none;\">+84 93 585 78 01</a></p>"
                + "<p>Facebook: <a href=\"https://m.me/694261313775146\" style=\"color: #1a237e; text-decoration: none;\">m.me/694261313775146</a></p>"
                + "<p>Email: <a href=\"mailto:" + fromEmail + "\" style=\"color: #1a237e; text-decoration: none;\">" + fromEmail + "</a></p>"
                + "</div>"
                + "</div>"
                + "</div>";
    }

    // Các phương thức khác (getLoginEmailTemplate, getOtpLoginEmailTemplate, etc.) có thể được giữ nguyên hoặc điều chỉnh tương tự
    // Ví dụ: Sử dụng getGeneralEmailTemplate cho các trường hợp cụ thể
    @Override
    public String getLoginEmailTemplate(String name, String loginTime) {
        String message = "A login attempt was made on your account at: <br><strong style=\"color: #1a237e;\">" + loginTime
                + "</strong><br>If this was you, no further action is required. If you suspect any unauthorized access, please contact our support team.";
        return getGeneralEmailTemplate(name, message, "Login Attempt Notification");
    }

    @Override
    public String getOtpLoginEmailTemplate(String name, String otp) {
        String message = "Thank you for choosing Global Link. Use the following OTP to complete your login procedures. The OTP is valid for "
                + OTPService.OTP_EXPIRY_MINUTES + " minutes: <br><h2 style=\"background: #1a237e; margin: 20px 0; width: max-content; padding: 10px 20px; color: #fff; border-radius: 4px;\">"
                + otp + "</h2>";
        return getGeneralEmailTemplate(name, message, "OTP for Login");
    }

    @Override
    public String getPasswordResetOtpRequestTemplate(String name, String otp) {
        String message = "We received a request to reset your password. Use the following OTP to verify your identity. This OTP is valid for "
                + OTPService.OTP_EXPIRY_MINUTES + " minutes: <br><h2 style=\"background: #1a237e; margin: 20px 0; width: max-content; padding: 10px 20px; color: #fff; border-radius: 4px;\">"
                + otp + "</h2><br>If you didn't request this password reset, please ignore this email or contact support immediately.";
        return getGeneralEmailTemplate(name, message, "Password Reset Request");
    }

    @Override
    public String getPasswordResetSuccessTemplate(String name, String resetTime) {
        String message = "Your password was successfully changed on: <br><strong style=\"color: #1a237e;\">" + resetTime
                + "</strong><br>If you didn't make this change, please contact our support team immediately. For security reasons, we recommend using a strong, unique password and enabling two-factor authentication.";
        return getGeneralEmailTemplate(name, message, "Password Changed");
    }

    @Override
    public String getEmailOrderCompleteTemplate(String name, String transactionCode, Double amountInCoin, Date time, String transactionStatus, String ticketProductName, Integer quantity, String ticketProductItemCode, List<TicketProductInfo> randomInfos) {
        StringBuilder builder = new StringBuilder();
        builder.append("<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">")
                .append("<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">")
                .append("<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">")
                .append("<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==\" alt=\"Global Link Logo\" style=\"height: 50px; margin-bottom: 10px;\">")
                .append("<h1 style=\"font-size: 1.8em; color: #1a237e; margin: 10px 0;\">Global Link</h1>")
                .append("</div>")
                .append("<div style=\"padding: 0 20px; font-size: 1em; color: #333;\">")
                .append("<p><strong>Hi ").append(name).append(",</strong></p>")
                .append("<p>Your order confirmation: </p>")
                .append("<ul style=\"list-style: none; padding-left: 0; color: #555;\">")
                .append("<li><strong>Transaction ID: </strong> ").append(transactionCode).append("</li>")
                .append("<li><strong>Amount: </strong> ").append(amountInCoin).append("</li>")
                .append("<li><strong>Order time: </strong> ").append(time).append("</li>")
                .append("<li><strong>Order item: </strong> ").append(ticketProductName).append("&nbsp;&nbsp;&nbsp;<span style='color:blue; font-weight: bold;'>").append(quantity).append("</span></li>")
                .append("<li><strong>Item code: </strong> ").append(ticketProductItemCode).append("</li>")
                .append("<li><strong>Order status:</strong> <span style='color: green; font-weight: bold;'>").append(transactionStatus).append("</span></li>")
                .append("<li><strong>Item details: </strong></li>")
                .append("</ul>");

        if (randomInfos != null && !randomInfos.isEmpty()) {
            builder.append("<div style='margin-top: 10px;'>")
                    .append("<p style='font-weight: bold;'>Item information:</p>")
                    .append("<pre style='background-color: #f9f9f9; padding: 10px; border: 1px solid #ddd; border-radius: 4px; overflow-x: auto; font-size: 14px;'>")
                    .append("UID | PASS | 2FA | MAIL | PASS MAIL | MAIL VERIFY\n");

            for (TicketProductInfo info : randomInfos) {
                builder.append(String.format("%s | %s | %s | %s | %s | %s\n",
                        Optional.ofNullable(info.getUid()).orElse(""),
                        Optional.ofNullable(info.getPass()).orElse(""),
                        Optional.ofNullable(info.getTwoFA()).orElse(""),
                        Optional.ofNullable(info.getMail()).orElse(""),
                        Optional.ofNullable(info.getPassMail()).orElse(""),
                        Optional.ofNullable(info.getMailVerify()).orElse("")
                ));
            }

            builder.append("</pre></div>");
        }

        builder.append("<p>Thanks for choosing Global Link.</p>")
                .append("</div>")
                .append("<hr style=\"border: none; border-top: 1px solid #ddd; margin: 30px 0;\" />")
                .append("<div style=\"text-align: center; font-size: 0.9em; color: #888;\">")
                .append("<p><strong>GlobalLink Media, LLC</strong></p>")
                .append("<p>131 Continental Dr, Suite 305, Newark, DE 19713, US</p>")
                .append("<p>Contact Us:</p>")
                .append("<p>WhatsApp: <a href=\"https://wa.me/84935857801\" style=\"color: #1a237e; text-decoration: none;\">+84 93 585 78 01</a></p>")
                .append("<p>Facebook: <a href=\"https://m.me/694261313775146\" style=\"color: #1a237e; text-decoration: none;\">m.me/694261313775146</a></p>")
                .append("<p>Email: <a href=\"mailto:").append(fromEmail).append("\" style=\"color: #1a237e; text-decoration: none;\">").append(fromEmail).append("</a></p>")
                .append("</div>")
                .append("</div></div>");

        return builder.toString();
    }

    @Override
    public String getEmailApologizeForBalanceErrorTemplate(String name, String transactionCode, double amount, Date correctionTime) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==\" alt=\"Global Link Logo\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "<h1 style=\"font-size: 1.8em; color: #1a237e; margin: 10px 0;\">Global Link</h1>"
                + "</div>"
                + "<div style=\"padding: 0 20px; font-size: 1em; color: #333;\">"
                + "<p><strong>Hi " + name + ",</strong></p>"
                + "<p>We sincerely apologize for the inconvenience caused due to a system error during your recent top-up transaction.</p>"
                + "<p>Our technical team has resolved the issue and manually updated your balance as follows:</p>"
                + "<ul style=\"list-style: none; padding-left: 0; color: #555;\">"
                + "<li><strong>Transaction ID: </strong> " + transactionCode + "</li>"
                + "<li><strong>Amount credited: </strong> " + amount + " coin(s)</li>"
                + "<li><strong>Correction time: </strong> " + correctionTime + "</li>"
                + "</ul>"
                + "<p>Thank you for your patience and understanding. If you have any further concerns, feel free to contact our support team.</p>"
                + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 30px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p><strong>GlobalLink Media, LLC</strong></p>"
                + "<p>131 Continental Dr, Suite 305, Newark, DE 19713, US</p>"
                + "<p>Contact Us:</p>"
                + "<p>WhatsApp: <a href=\"https://wa.me/84935857801\" style=\"color: #1a237e; text-decoration: none;\">+84 93 585 78 01</a></p>"
                + "<p>Facebook: <a href=\"https://m.me/694261313775146\" style=\"color: #1a237e; text-decoration: none;\">m.me/694261313775146</a></p>"
                + "<p>Email: <a href=\"mailto:" + fromEmail + "\" style=\"color: #1a237e; text-decoration: none;\">" + fromEmail + "</a></p>"
                + "</div>"
                + "</div>"
                + "</div>";
    }
}