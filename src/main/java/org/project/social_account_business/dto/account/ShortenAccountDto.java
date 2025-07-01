package org.project.social_account_business.dto.account;

import lombok.Data;

@Data
public class ShortenAccountDto {
    private Long id;
    private String email;
    private String phone;
    private String username;
}
