package org.project.social_account_business.form.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderForm {
    @Schema(description = "The ID of the ticket product being ordered", example = "1234567890")
    @NotNull(message = "Ticket product ID cannot be null")
    private Long ticketProductId;
    @Schema(description = "The ID of the account placing the order", example = "1234567890")
    @NotNull(message = "Account ID cannot be null")
    private Long accountId;
    @Schema(description = "The quantity of the ticket product being ordered", example = "2")
    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;
}
