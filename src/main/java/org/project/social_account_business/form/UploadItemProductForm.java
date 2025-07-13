package org.project.social_account_business.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class UploadItemProductForm {
    private String name;
    private BigDecimal price;
    private String description;
    private Integer maxPurchasePerAccount;

    public UploadItemProductForm() {
    }
}
