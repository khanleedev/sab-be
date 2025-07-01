package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Table(name = TablePrefix.PREFIX + "transactions",
        indexes = {
                @Index(name = "idx_transaction_id", columnList = "transaction_id"),
                @Index(name = "idx_transaction_order_status", columnList = "order_status"),
                @Index(name = "idx_transaction_type", columnList = "transaction_type"),
                @Index(name = "idx_transaction_transaction_code", columnList = "transaction_code"),
        })

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Transaction extends Auditable<String> {
    @Id
    @SystemId
    private Long id;
    @Column(name = "transaction_id")
    private Long transactionId;
    @Column(name = "amount_in_coin")
    private Double amountInCoin;
    @Column(name = "amount_in_cash")
    private BigDecimal amountInCash;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private TransactionStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    @Column(name = "transaction_code")
    private String transactionCode;
}
