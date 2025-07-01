package org.project.social_account_business.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.social_account_business.dto.ABasicAdminDto;
import org.project.social_account_business.dto.account.ShortenAccountDto;
import org.project.social_account_business.dto.ticket_product.ShortenTicketProductDto;
import org.project.social_account_business.dto.transaction.TransactionForOrderDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto extends ABasicAdminDto {
    private Long id;
    private ShortenTicketProductDto ticketProduct;
    private ShortenAccountDto account;
    private Integer quantity;
    private Double totalPrice;
}
