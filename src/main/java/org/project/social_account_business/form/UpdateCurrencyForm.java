package org.project.social_account_business.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCurrencyForm {
    @Schema(description = "The ID of the currency", example = "1234567890")
    @NotNull(message = "Currency ID cannot be null")
    private long id;
    @Schema(description = "The rate of the currency", example = "0.01")
    @NotNull(message = "Currency rate cannot be null")
    private double rate;
}
