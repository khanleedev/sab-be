package org.project.social_account_business.form.payment_transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreatePaymentTransactionForm {
    @Schema(description = "The transaction ID from sepay", example = "SEPAY123456")
    @NotEmpty(message = "Sepay transaction ID cannot be empty")
    @JsonProperty("sepayTransactionId")
    private String sepayTransactionId;
    @Schema(description = "The transaction gateway from return callback api of sepay", example = "sepay")
    @NotEmpty(message = "Gateway cannot be empty")
    @JsonProperty("gateway")
    private String gateway;
    @Schema(description = "The transaction date", example = "2023-10-01")
    @NotEmpty(message = "Transaction date cannot be empty")
    @JsonProperty("transactionDate")
    private Date transactionDate;
    @Schema(description = "The account number of the user", example = "5134123211")
    @NotEmpty(message = "Account number cannot be empty")
    @JsonProperty("accountNumber")
    private String accountNumber;
    @Schema(description = "The code of the transaction", example = "123456")
    @NotEmpty(message = "Code cannot be empty")
    @JsonProperty("code")
    private String code;
    @Schema(description = "The description of the transaction", example = "Payment for service")
    @JsonProperty("description")
    private String description;
    @Schema(description = "The amount of money in the user send in the transaction", example = "1000.00")
    @NotEmpty(message = "Amount in cannot be empty")
    @JsonProperty("amountIn")
    private BigDecimal amountIn;
    @Schema(description = "The accumulated amount of money in the transaction", example = "1000.00")
    @JsonProperty("accumulated")
    private BigDecimal accumulated;
    @Schema(description = "The reference number of the transaction", example = "REF123456")
    @JsonProperty("referenceNumber")
    private String referenceNumber;
    @Schema(description = "The callback URL of the transaction", example = "https://example.com/callback")
    @NotEmpty(message = "Callback URL cannot be empty")
    @JsonProperty("callbackUrl")
    private String callbackUrl;
    @Schema(description = "The ID of the account", example = "21321321321")
    @NotEmpty(message = "Account ID cannot be empty")
    @JsonProperty("accountId")
    private Long accountId;
    @Schema(description = "Currency code used in sepay", example = "vnd")
    @NotEmpty(message = "Currency code cannot be empty")
    @JsonProperty("currencyCode")
    private String currencyCode;
}
