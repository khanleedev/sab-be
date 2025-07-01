package org.project.social_account_business.dto.payment_transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.social_account_business.dto.ABasicAdminDto;

import org.project.social_account_business.dto.account.ShortenAccountDto;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactionDto extends ABasicAdminDto {
    private Long id;
    private String gateway;
    private String transactionDate;
    private String accountNumber;
    private ShortenAccountDto account;
    private BigDecimal amountIn;
    private BigDecimal accumulated;
    private String code;
    private String description;
    private String referenceNumber;
    private String callbackUrl;
    private String sepayTransactionId;
}
