package org.project.social_account_business.form.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBalanceForm {
    @NotNull(message = "Account ID is required")
    @Schema(description = "The ID of the account", example = "12345")
    private Long accountId;
    @NotNull
    @Schema(description = "The new balance amount", example = "1000000")
    private Double balance;
    @NotEmpty(message = "Transaction code is required")
    @Schema(description = "The transaction code", example = "TRX123")
    private String transactionCode;
}
