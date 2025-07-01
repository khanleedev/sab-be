package org.project.social_account_business.form.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTransactionForOrderForm {
    @Schema(description = "ID of the order associated with the transaction", example = "1234567890")
    @NotNull(message = "Order ID cannot be null")
    private Long orderId;
    @Schema(description = "The amount of money in coin costs", example = "100.0")
    @NotNull(message = "Amount in coin cannot be null")
    private Double amountInCoin;
}
