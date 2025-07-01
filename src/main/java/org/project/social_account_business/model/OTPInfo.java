package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TablePrefix.PREFIX + "otps", indexes = {
        @Index(name = "idx_otp_email", columnList = "email"),
        @Index(name = "idx_otp_generated_at", columnList = "generated_at")
})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EntityListeners(AuditingEntityListener.class)
public class OTPInfo extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "otp")
    private String otp;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    public OTPInfo(String email, String otp, LocalDateTime generatedAt) {
        this.email = email;
        this.otp = otp;
        this.generatedAt = generatedAt;
    }
}
