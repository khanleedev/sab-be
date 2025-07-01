package org.project.social_account_business.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.service.email.EmailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncService {
    private final EmailService emailService;

    public AsyncService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async  // Let Spring handle the async execution
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void sendEmail(String to, String text, String subject, boolean html) {
        try {
            emailService.sendEmail(to, text, subject, html);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }
}
