package org.project.social_account_business.form.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionForPaymentForm {
    @Schema(description = "The amount of money in cash", example = "100.0")
    @NotNull(message = "Amount in cash cannot be null")
    private BigDecimal amountInVnd;
}
