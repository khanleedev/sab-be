package org.project.social_account_business.dto.ticket_product_info;

import lombok.Data;

@Data
public class TicketProductInfoDto {
    private Long id;
    private String uid;
    private String pass;
    private String twoFA;
    private String mail;
    private String passMail;
    private String mailVerify;
    private Boolean isSold;


    public TicketProductInfoDto() {
        // Default constructor
    }
}
