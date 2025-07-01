package org.project.social_account_business.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.social_account_business.dto.ABasicAdminDto;
import org.project.social_account_business.dto.payment_transaction.ShortenPaymentTransactionDto;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionForPaymentDto extends ABasicAdminDto {
    private Long id;
    private Double amountInCash;
    private String orderStatus;
}
