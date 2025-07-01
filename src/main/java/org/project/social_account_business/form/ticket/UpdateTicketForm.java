package org.project.social_account_business.form.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateTicketForm {
    @Schema(name = "id", example = "7812372178932", required = true)
    private Long id;
    @Schema(name = "title", example = "This is new title")
    private String title;
}
