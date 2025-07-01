package org.project.social_account_business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ABasicAdminDto {
    @Schema(name = "id")
    private Long id;
    @Schema(name = "status")
    private Integer status;
    @Schema(name = "modifiedDate")
    private LocalDateTime modifiedDate;
    @Schema(name = "createdDate")
    private LocalDateTime createdDate;
    @Schema(name = "createdBy")
    private String createdBy;
}
