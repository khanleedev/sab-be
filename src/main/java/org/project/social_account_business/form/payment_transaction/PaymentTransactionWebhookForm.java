package org.project.social_account_business.form.payment_transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentTransactionWebhookForm {

    private String id;
    private String gateway;

    @JsonProperty("transactionDate")
    private Date transactionDate;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("subAccount")
    private String subAccount;

    private String code;
    private String content;

    @JsonProperty("transferType")
    private String transferType;

    @JsonProperty("transferAmount")
    private Long transferAmount;

    private Long accumulated;

    @JsonProperty("referenceCode")
    private String referenceCode;

    private String description;
}
