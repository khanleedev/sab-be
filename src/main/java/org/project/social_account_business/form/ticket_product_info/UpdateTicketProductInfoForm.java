package org.project.social_account_business.form.ticket_product_info;

import lombok.Data;

@Data
public class UpdateTicketProductInfoForm {
    private Long id;
    private String uid;
    private String pass;
    private String twoFA;
    private String mail;
    private String passMail;
    private String mailVerify;
}
