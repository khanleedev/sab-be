package org.project.social_account_business.dto.payment_transaction;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PaypalResponseDto {
    private String orderId;
    private String approvalLink;
    private String status;
}
