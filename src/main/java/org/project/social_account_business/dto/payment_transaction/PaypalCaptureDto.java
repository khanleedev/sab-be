package org.project.social_account_business.dto.payment_transaction;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PaypalCaptureDto {
    private String orderId;
    private String status;
    private String amount;
    private String currency;
    private String userId;
}
