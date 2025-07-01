package org.project.social_account_business.form.transaction;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class CreateTransactionForm {
    @Schema(name = "orderId", example = "7812372178932", required = true)
    private Long orderId;
}
