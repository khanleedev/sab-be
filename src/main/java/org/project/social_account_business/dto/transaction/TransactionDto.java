package org.project.social_account_business.dto.transaction;

import lombok.Getter;
import lombok.Setter;
import org.project.social_account_business.dto.ABasicAdminDto;
import org.project.social_account_business.dto.account.AccountDto;
import org.project.social_account_business.dto.account.ShortenAccountDto;
import org.project.social_account_business.dto.order.ShortenOrderDto;
import org.project.social_account_business.dto.payment_transaction.ShortenPaymentTransactionDto;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto extends ABasicAdminDto {
    private Long id;
    private Long transactionId;
    private BigDecimal amountInCash;
    private Double amountInCoin;
    private String orderStatus;
    private String transactionType;
    private String transactionCode;
}