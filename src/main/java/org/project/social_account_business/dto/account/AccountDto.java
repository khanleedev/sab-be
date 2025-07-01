package org.project.social_account_business.dto.account;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.project.social_account_business.dto.ABasicAdminDto;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AccountDto extends ABasicAdminDto {
    Long id;
    String username;
    String email;
    String phoneNo;
    Long balance;
    public AccountDto(Long id, String username, String email, String phoneNo, Long balance) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.balance = balance;
    }
}
