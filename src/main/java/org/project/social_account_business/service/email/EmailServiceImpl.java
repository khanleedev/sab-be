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

    @Override
    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text, boolean html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, html);
            mailSender.send(message);

            log.info("Sent email to {}", to);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public String getLoginEmailTemplate(String name, String loginTime) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<a href=\"https://onestopbank.netlify.app/\" style=\"text-decoration: none;\">"
                + "<img src=\"https://onestopbank.netlify.app/assets/onestoplogo.jpg\" alt=\"OneStopBank\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "</a>" + "<h1 style=\"font-size: 1.8em; color: #3f51b5; margin: 10px 0;\">TEST</h1>" + "</div>"
                + "<div style=\"padding: 20px;\">" + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #333;\">A login attempt was made on your account at:</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Time: <strong style=\"color: #3f51b5;\">" + loginTime
                + "</strong></p>"
                + "<p style=\"font-size: 1em; color: #333;\">If this was you, no further action is required. If you suspect any unauthorized access, please change your password immediately and contact our support team.</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Regards,<br />The Test Team</p>" + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>Need help? Contact our support team:</p>"
                + "<p>Email: <a href=\"mailto:lhktnt@gmail.com\" style=\"color: #3f51b5; text-decoration: none;\">lhktnt@gmail.com</a></p>"
                + "<div style=\"margin-top: 20px;\">"
                + "<p style=\"font-size: 1em; color: #333;\">Show your support here ❤️</p>"
                + "</div>" + "</div>" + "</div>" + "</div>";
    }

    @Override
    public String getOtpLoginEmailTemplate(String name, String otp) {

        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<a href=\"https://onestopbank.netlify.app/\" style=\"text-decoration: none;\">"
                + "<img src=\"https://onestopbank.netlify.app/assets/onestoplogo.jpg\" alt=\"OneStopBank\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "</a>" + "<h1 style=\"font-size: 1.8em; color: #3f51b5; margin: 10px 0;\">TEST</h1>" + "</div>"
                + "<div style=\"padding: 20px;\">" + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #333;\">Thank you for choosing OneStopBank. Use the following OTP to complete your login procedures. The OTP is valid for "
                + OTPService.OTP_EXPIRY_MINUTES + " minutes:</p>"
                + "<h2 style=\"background: #3f51b5; margin: 20px 0; width: max-content; padding: 10px 20px; color: #fff; border-radius: 4px;\">"
                + otp + "</h2>" + "<p style=\"font-size: 1em; color: #555;\">Regards,<br />The Test Team</p>"
                + "</div>" + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>Need help? Contact our support team:</p>"
                + "<p>Email: <a href=\"mailto:" + fromEmail + "\" style=\"color: #3f51b5; text-decoration: none;\">" + fromEmail + "</a></p>"
                + "<div style=\"margin-top: 20px;\">"
                + "<p style=\"font-size: 1em; color: #333;\">Show your support here ❤️</p>"
                + "</div>" + "</div>" + "</div>" + "</div>";
    }

    @Override
    public String getPasswordResetOtpRequestTemplate(String name, String otp) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<a href=\"https://onestopbank.netlify.app/\" style=\"text-decoration: none;\">"
                + "<img src=\"https://onestopbank.netlify.app/assets/onestoplogo.jpg\" alt=\"OneStopBank\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "</a>" + "<h1 style=\"font-size: 1.8em; color: #3f51b5; margin: 10px 0;\">Password Reset Request</h1>" + "</div>"
                + "<div style=\"padding: 20px;\">" + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #333;\">We received a request to reset your password. Use the following OTP to verify your identity. This OTP is valid for "
                + OTPService.OTP_EXPIRY_MINUTES + " minutes:</p>"
                + "<h2 style=\"background: #3f51b5; margin: 20px 0; width: max-content; padding: 10px 20px; color: #fff; border-radius: 4px;\">"
                + otp + "</h2>"
                + "<p style=\"font-size: 1em; color: #333;\">If you didn't request this password reset, please ignore this email or contact support immediately.</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Regards,<br />The Security Team</p>"
                + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>Need help? Contact our support team:</p>"
                + "<p>Email: <a href=\"mailto:" + fromEmail + "\" style=\"color: #3f51b5; text-decoration: none;\">" + fromEmail + "</a></p>"
                + "</div>" + "</div>" + "</div>";
    }

    @Override
    public String getPasswordResetSuccessTemplate(String name, String resetTime) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<a href=\"https://onestopbank.netlify.app/\" style=\"text-decoration: none;\">"
                + "<img src=\"https://onestopbank.netlify.app/assets/onestoplogo.jpg\" alt=\"OneStopBank\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "</a>" + "<h1 style=\"font-size: 1.8em; color: #3f51b5; margin: 10px 0;\">Password Changed</h1>" + "</div>"
                + "<div style=\"padding: 20px;\">" + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #333;\">Your password was successfully changed on:</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Time: <strong style=\"color: #3f51b5;\">" + resetTime + "</strong></p>"
                + "<p style=\"font-size: 1em; color: #333;\">If you didn't make this change, please contact our support team immediately.</p>"
                + "<p style=\"font-size: 1em; color: #555;\">For security reasons, we recommend:</p>"
                + "<ul style=\"font-size: 1em; color: #555; padding-left: 20px;\">"
                + "<li>Using a strong, unique password</li>"
                + "<li>Enabling two-factor authentication</li>"
                + "<li>Regularly updating your password</li>"
                + "</ul>"
                + "<p style=\"font-size: 1em; color: #555;\">Regards,<br />The Security Team</p>"
                + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>Need help? Contact our support team:</p>"
                + "<p>Email: <a href=\"mailto:" + fromEmail + "\" style=\"color: #3f51b5; text-decoration: none;\">" + fromEmail + "</a></p>"
                + "</div>" + "</div>" + "</div>";
    }

    @Override
    public String getEmailOrderCompleteTemplate(String name,
                                                String transactionCode,
                                                Double amountInCoin,
                                                Date time,
                                                String transactionStatus,
                                                String ticketProductName,
                                                Integer quantity,
                                                String ticketProductItemCode,
                                                List<TicketProductInfo> randomInfos) {
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("<h3 style=\"margin-top: 30px; color: #333;\">Purchased Account Info</h3>");
        tableBuilder.append("<table style=\"width: 100%; border-collapse: collapse; font-size: 14px;\">");
        tableBuilder.append("<thead><tr style=\"background-color: #f2f2f2; text-align: left;\">")
                .append("<th style=\"padding: 8px; border: 1px solid #ddd;\">UID</th>")
                .append("<th style=\"padding: 8px; border: 1px solid #ddd;\">PASS</th>")
                .append("<th style=\"padding: 8px; border: 1px solid #ddd;\">2FA</th>")
                .append("<th style=\"padding: 8px; border: 1px solid #ddd;\">MAIL</th>")
                .append("<th style=\"padding: 8px; border: 1px solid #ddd;\">PASS MAIL</th>")
                .append("<th style=\"padding: 8px; border: 1px solid #ddd;\">MAIL VERIFY</th>")
                .append("</tr></thead><tbody>");

        for (TicketProductInfo info : randomInfos) {
            tableBuilder.append("<tr>")
                    .append("<td style=\"padding: 8px; border: 1px solid #ddd;\">").append(info.getUid()).append("</td>")
                    .append("<td style=\"padding: 8px; border: 1px solid #ddd;\">").append(info.getPass()).append("</td>")
                    .append("<td style=\"padding: 8px; border: 1px solid #ddd;\">").append(info.getTwoFA() == null ? "-" : info.getTwoFA()).append("</td>")
                    .append("<td style=\"padding: 8px; border: 1px solid #ddd;\">").append(info.getMail() == null ? "-" : info.getMail()).append("</td>")
                    .append("<td style=\"padding: 8px; border: 1px solid #ddd;\">").append(info.getPassMail() == null ? "-" : info.getPassMail()).append("</td>")
                    .append("<td style=\"padding: 8px; border: 1px solid #ddd;\">").append(info.getMailVerify() == null ? "-" : info.getMailVerify()).append("</td>")
                    .append("</tr>");
        }

        tableBuilder.append("</tbody></table>");

        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"text-align: center; padding-bottom: 20px;\">"
                + "<img src=\"https://cdn-icons-png.flaticon.com/512/845/845646.png\" alt=\"Success\" style=\"width: 80px;\">"
                + "<h1 style=\"font-size: 1.8em; color: #2ecc71; margin: 20px 0;\">ORDER CONFIRMATION SUCCESS!</h1>"
                + "</div>"
                + "<div style=\"padding: 0 20px; font-size: 1em; color: #333;\">"
                + "<p><strong>Hi " + name + ",</strong></p>"
                + "<p>Your order confirmation: </p>"
                + "<ul style=\"list-style: none; padding-left: 0; color: #555;\">"
                + "<li><strong>Transaction ID: </strong> " + transactionCode + "</li>"
                + "<li><strong>Amount: </strong> " + amountInCoin + "</li>"
                + "<li><strong>Order time: </strong> " + time + "</li>"
                + "<li><strong>Order item: </strong> " + ticketProductName + "&nbsp;&nbsp;&nbsp;"
                + "<span style=\"color:blue; font-weight: bold;\">" + quantity + "</span></li>"
                + "<li><strong>Item code: </strong> " + ticketProductItemCode + "</li>"
                + "<li><strong>Order status:</strong> <span style=\"color: green; font-weight: bold;\">" + transactionStatus + "</span></li>"
                + "<li><strong>Item details: </strong></li>"
                + "</ul>"
                + tableBuilder
                + "<p>Thanks for choosing our service.</p>"
                + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 30px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>📧 Any confusion please contact by this email:</p>"
                + "<p>Email: <a href=\"mailto:" + fromEmail + "\" style=\"color: #3f51b5; text-decoration: none;\">" + fromEmail + "</a></p>"
                + "</div>"
                + "</div></div>";
    }


    @Override
    public String getEmailApologizeForBalanceErrorTemplate(String name, String transactionCode, double amount, Date correctionTime) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"text-align: center; padding-bottom: 20px;\">"
                + "<img src=\"https://cdn-icons-png.flaticon.com/512/564/564619.png\" alt=\"Error\" style=\"width: 80px;\">"
                + "<h1 style=\"font-size: 1.8em; color: #e74c3c; margin: 20px 0;\">APOLOGIES FOR THE TOP-UP ISSUE</h1>"
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
                + "<div style=\"text-align: center; margin-top: 30px;\">"
                + "</div>"
                + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 30px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>📧 For support, contact us via email:</p>"
                + "<p>Email: <a href=\"mailto:" + fromEmail + "\" style=\"color: #3f51b5; text-decoration: none;\">" + fromEmail + "</a></p>"
                + "</div>"
                + "</div>"
                + "</div>";
    }
}
