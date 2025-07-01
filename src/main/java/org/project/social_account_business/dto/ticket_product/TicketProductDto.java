package org.project.social_account_business.dto.ticket_product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.social_account_business.dto.ABasicAdminDto;
import org.project.social_account_business.dto.ticket.TicketDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketProductDto extends ABasicAdminDto {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private Double price;
    private TicketDto ticket;
    private String itemCode;
    private Integer maxPurchasePerAccount;
}