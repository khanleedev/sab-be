package org.project.social_account_business.form.ticket_product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTicketProductForm {
    @Schema(description = "Id of the product", example = "1")
    @NotNull(message = "Id is required")
    private Long id;
    @Schema(description = "Name of the product", example = "Product 1")
    private String name;
    @Schema(description = "Description of the product", example = "Description of product 1")
    private String description;
    @Schema(description = "Quantity of the product", example = "10")
    private Integer quantity;
    @Schema(description = "Price of the product", example = "10")
    private Long price;
    @Schema(description = "Max purchase per account", example = "1")
    private Integer maxPurchasePerAccount;
}
