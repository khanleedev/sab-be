package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = TablePrefix.PREFIX + "payment_transactions", indexes = {
        @Index(name = "idx_payment_transaction_account_id", columnList = "account_id"),
        @Index(name = "idx_payment_transaction_gateway", columnList = "gateway"),
        @Index(name = "idx_payment_transaction_date", columnList = "transaction_date"),
        @Index(name = "idx_payment_transaction_reference_number", columnList = "reference_number"),
        @Index(name = "idx_payment_transaction_account_number", columnList = "account_number"),
})
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class PaymentTransaction extends Auditable<String> {
    @Id
    @SystemId
    private Long id;
    private String gateway;
    @Column(name = "transaction_date")
    private Date transactionDate;
    @Column(name = "account_number")
    private String accountNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    private Account account;
    @Column(name = "amount_in")
    private BigDecimal amountIn;
    private BigDecimal accumulated;
    private String code;
    private String description;
    @Column(name = "reference_number")
    private String referenceNumber;
    @Column(name = "callback_url")
    private String callbackUrl;
    @Column(name = "sepay_transaction_id")
    private String sepayTransactionId;
}
