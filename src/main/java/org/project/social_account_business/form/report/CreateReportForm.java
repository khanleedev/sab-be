package org.project.social_account_business.form.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReportForm {
    @Schema(description = "The report content created by user", example = "This is report content.")
    @NotEmpty(message = "Report content cannot be empty")
    private String content;
    @Schema(description = "The ID of the user making report.", example = "1234567890")
    @NotNull(message = "User ID cannot be null")
    private Long accountId;
}
