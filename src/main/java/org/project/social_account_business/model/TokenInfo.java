package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Setter
@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TablePrefix.PREFIX + "token_infos",
        indexes = {
                @Index(name = "idx_token_info_account_id", columnList = "account_id"),
                @Index(name = "idx_token_info_token", columnList = "token"),
                @Index(name = "idx_token_info_expiry_at", columnList = "expiry_at"),
                @Index(name = "idx_token_info_type", columnList = "token_type"),
        })
@EntityListeners(AuditingEntityListener.class)
public class TokenInfo extends Auditable<String> {
    @Id
    @SystemId
    private Long id;
    @Column(unique = true)
    private String token;
    @Column(name = "expiry_at")
    private Date expiryAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private TokenType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public TokenInfo(String token, Date expiryAt, Account account, TokenType type) {
        this.token = token;
        this.expiryAt = expiryAt;
        this.account = account;
        this.type = type;
    }
}
