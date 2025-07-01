package org.project.social_account_business.form.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketForm {
    @Schema(description = "Title of the ticket", example = "This is a ticket")
    @NotEmpty(message = "Title is required")
    @JsonProperty("title")
    private String title;
}

