package org.project.social_account_business.dto.order;

import lombok.Data;
import org.project.social_account_business.dto.account.ShortenAccountDto;
import org.project.social_account_business.dto.ticket_product.ShortenTicketProductDto;

@Data
public class ShortenOrderDto {
    private Long id;
    private Integer quantity;
    private Double totalPrice;
    private ShortenTicketProductDto ticketProduct;
    private ShortenAccountDto account;

}
