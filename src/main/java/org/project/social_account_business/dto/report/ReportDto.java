package org.project.social_account_business.dto.report;

import lombok.Data;
import org.project.social_account_business.dto.ABasicAdminDto;
import org.project.social_account_business.dto.account.ShortenAccountDto;

@Data
public class ReportDto extends ABasicAdminDto {
    private Long id;
    private ShortenAccountDto account;
    private String content;
}
