package org.project.social_account_business.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.exception.OtpRetryLimitExceededException;
import org.project.social_account_business.model.OTPInfo;
import org.project.social_account_business.repository.OTPInfoRepository;
import org.project.social_account_business.service.account.AccountService;
import org.project.social_account_business.service.email.EmailService;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
public class OTPService {
    public static final int OTP_ATTEMPTS_LIMIT = 3;
    public static final int OTP_EXPIRY_MINUTES = 5;
    public static final int OTP_RESET_WAITING_TIME_MINUTES = 10;
    public static final int OTP_RETRY_LIMIT_WINDOW_MINUTES = 15;

    private final SecureRandom secureRandom;
    private final CacheManager cacheManager;
    private final EmailService emailService;
    private final OTPInfoRepository otpInfoRepository;
    private final AccountService accountService;
    private final AsyncService asyncService;

    private LocalDateTime otpLimitReachedTime = null;
    private final String otpSubject = "Email Verification OTP";

    public OTPService(CacheManager cacheManager, EmailService emailService,
                      OTPInfoRepository otpInfoRepository, AccountService accountService, AsyncService asyncService)
            throws NoSuchAlgorithmException {
        this.asyncService = asyncService;
        this.secureRandom = SecureRandom.getInstanceStrong();
        this.cacheManager = cacheManager;
        this.emailService = emailService;
        this.otpInfoRepository = otpInfoRepository;
        this.accountService = accountService;
    }

    @Transactional
    public String sendEmailVerificationPasswordResetOTP(String email) {
        val account = accountService.findAccountByEmail(email);
        checkOtpAttemptLimit(email);

        String otp = generateNewOTP(email);

        sendOTPEmail(email, account.getUsername(), otp);

        log.info("[OTP Service] Sent password reset OTP to {}", email);
        return otp;
    }

    @Transactional
    public void sendEmailVerificationOTPForRegister(String email, String username) {
        if(accountService.existsAccountByEmail(email)) {
            throw new BadRequestException("[OTP Service] Email already registered", ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        checkOtpAttemptLimit(email);

        String otp = generateNewOTP(email);

        sendOTPEmail(email, username, otp);

        log.info("[OTP Service] Sent verification OTP to {}", email);
    }

    @Transactional
    public boolean verifyEmailOTP(String email, String otp) {
        OTPInfo otpInfo = otpInfoRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new NotFoundException("[OTP Service] OTP not found.", ErrorCode.OTP_NOT_FOUND));

        if (isOtpExpired(otpInfo)) {
            otpInfoRepository.deleteByOtpAndEmail(otp, email);
            throw new BadRequestException("[OTP Service] Expired OTP verification code", ErrorCode.EXPIRED_OTP);
        }

        otpInfoRepository.deleteByOtpAndEmail(otp, email);
        resetOtpAttempts(email);
        return true;
    }

    @Transactional
    protected String generateNewOTP(String email) {
        Optional<OTPInfo> existingOtp = otpInfoRepository.findByEmail(email);

        String otp = String.format("%06d", secureRandom.nextInt(999999));

        OTPInfo otpInfo = existingOtp.orElse(new OTPInfo());
        otpInfo.setEmail(email);
        otpInfo.setOtp(otp);
        otpInfo.setGeneratedAt(LocalDateTime.now());

        otpInfoRepository.save(otpInfo);
        return otp;
    }

    private void sendOTPEmail(String email, String username, String otp) {
        String maskedUsername = username.length() > 3 ?
                "xxx" + username.substring(3) :
                "xxx";
        String emailContent = emailService.getOtpLoginEmailTemplate(maskedUsername, otp);

        asyncService.sendEmail(email, otpSubject, emailContent, true);
    }

    private void sendOTPEmailForResetPassword(String email, String username, String otp) {
        String maskedUsername = username.length() > 3 ?
                "xxx" + username.substring(3) :
                "xxx";
        String emailContent = emailService.getPasswordResetOtpRequestTemplate(maskedUsername, otp);

        asyncService.sendEmail(email, otpSubject, emailContent, true);
    }

    private void checkOtpAttemptLimit(String email) {
        int attempts = getOtpAttempts(email);

        if (attempts >= OTP_ATTEMPTS_LIMIT) {
            if (isOtpResetWaitingTimeExceeded()) {
                resetOtpAttempts(email);
            } else {
                // Initialize time if null (first time hitting limit)
                if (otpLimitReachedTime == null) {
                    otpLimitReachedTime = LocalDateTime.now();
                }

                long waitingMinutes = OTP_RESET_WAITING_TIME_MINUTES -
                        ChronoUnit.MINUTES.between(
                                otpLimitReachedTime,
                                LocalDateTime.now()
                        );

                throw new OtpRetryLimitExceededException(
                        String.format("[OTP Service] You have requested OTP codes more than %d times. Please wait %d minutes to try again.",
                                OTP_ATTEMPTS_LIMIT,
                                waitingMinutes)
                );
            }
        }

        incrementOtpAttempts(email);
    }

    private boolean isOtpExpired(OTPInfo otpInfo) {
        return otpInfo.getGeneratedAt()
                .isBefore(LocalDateTime.now().minusMinutes(OTP_EXPIRY_MINUTES));
    }

    private void incrementOtpAttempts(String email) {
        val cache = cacheManager.getCache("otpAttempts");
        if (cache != null) {
            cache.put(email, getOtpAttempts(email) + 1);
        }
    }

    private void resetOtpAttempts(String email) {
        otpLimitReachedTime = null;
        val cache = cacheManager.getCache("otpAttempts");
        if (cache != null) {
            cache.put(email, 0);
        }
    }

    private int getOtpAttempts(String email) {
        val cache = cacheManager.getCache("otpAttempts");
        Integer attempts = cache != null ? cache.get(email, Integer.class) : null;
        return attempts != null ? attempts : 0;
    }

    private boolean isOtpResetWaitingTimeExceeded() {
        if (otpLimitReachedTime == null) {
            return true; // No limit reached yet
        }
        return otpLimitReachedTime.isBefore(
                LocalDateTime.now().minusMinutes(OTP_RESET_WAITING_TIME_MINUTES)
        );
    }

}