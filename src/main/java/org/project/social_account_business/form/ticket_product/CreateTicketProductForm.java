package org.project.social_account_business.form.ticket_product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateTicketProductForm {
    @Schema(description = "Name of the product", example = "Product 1")
    private String name;
    @Schema(description = "Description of the product", example = "Description of product 1")
    private String description;
    @Schema(description = "Quantity of the product", example = "10")
    @JsonIgnore
    private Integer quantity = 0;
    @Schema(description = "Price of the product", example = "10")
    private Double price;
    @Schema(description = "Ticket Id", example = "item_code_1")
    private Long ticketId;
    @Schema(description = "Max purchase per account", example = "1")
    private Integer maxPurchasePerAccount;
}
