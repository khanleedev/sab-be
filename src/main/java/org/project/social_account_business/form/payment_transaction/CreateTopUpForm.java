package org.project.social_account_business.form.payment_transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTopUpForm {
    @Schema(description = "The amount to be topped up", example = "1000000")
    @NotNull(message = "Amount is required")
    @JsonProperty("amount")
    private BigDecimal amount;
}
