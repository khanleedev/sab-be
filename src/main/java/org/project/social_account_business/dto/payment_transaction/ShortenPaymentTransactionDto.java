package org.project.social_account_business.dto.payment_transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortenPaymentTransactionDto{
    private Long id;
    private String transactionDate;
    private String accountNumber;
    private String referenceNumber;
    private String sepayTransactionId;
    private String callbackUrl;
}
