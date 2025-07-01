package org.project.social_account_business.form.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.project.social_account_business.validation.NumberField;

@Data
public class UpdateAccountForm {
    @NotNull(message = "id cant not be null")
    @Schema(name = "id", example = "7812372178932", required = true)
    @JsonProperty(value = "id")
    private Long id;
    @Schema(name = "phoneNo", example = "1234567890")
    @NumberField
    @JsonProperty(value = "phoneNo")
    private String phoneNo;
    @Schema(name = "userName", example = "user123")
    @JsonProperty(value = "username")
    private String username;
}
