package org.project.social_account_business.dto.ticket_product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShortenTicketProductDto {
    private Long id;
    private String name;
    private Double price;
    private String itemCode;
}
