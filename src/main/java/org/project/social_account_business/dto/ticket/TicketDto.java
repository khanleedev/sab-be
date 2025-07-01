package org.project.social_account_business.dto.ticket;

import lombok.*;
import org.project.social_account_business.dto.ABasicAdminDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto extends ABasicAdminDto{
    private Long id;
    private String title;
}
