package org.project.social_account_business.form.ticket_product_info;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTicketProductInfoForm {
    private String uid;
    private String pass;
    private String twoFA;
    private String mail;
    private String passMail;
    private String mailVerify;
    @NotNull(message = "Ticket product ID cannot be null")
    private Long ticketProductId;
}
