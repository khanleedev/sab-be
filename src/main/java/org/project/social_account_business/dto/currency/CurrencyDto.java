package org.project.social_account_business.dto.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.project.social_account_business.dto.ABasicAdminDto;

@Setter
@Getter
@AllArgsConstructor
public class CurrencyDto extends ABasicAdminDto {
    private Long id;
    private String code;
    private String name;
    private Double rate;
    private Double bonusRate;
}
