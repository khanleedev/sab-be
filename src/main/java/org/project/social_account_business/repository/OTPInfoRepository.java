package org.project.social_account_business.repository;

import org.project.social_account_business.model.OTPInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPInfoRepository extends JpaRepository<OTPInfo, Long> {
    @Query("SELECT o FROM OTPInfo o WHERE o.email = :email")
    Optional<OTPInfo> findByEmail(String email);
    void deleteByOtpAndEmail(String otp, String email);
    void deleteOTPInfoByEmail(String email);
    Optional<OTPInfo> findByEmailAndOtp(String email, String otp);
}
