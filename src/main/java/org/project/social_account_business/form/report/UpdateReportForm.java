package org.project.social_account_business.form.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateReportForm {
    @Schema(description = "The ID of the report to be updated", example = "1234567890")
    @NotEmpty(message = "Report ID cannot be empty")
    private long reportId;
    @Schema(description = "The report content created by user", example = "This is report content.")
    @NotEmpty(message = "Report content cannot be empty")
    private String content;
}
