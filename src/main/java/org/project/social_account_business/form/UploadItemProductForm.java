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
    private int quantity;
    private BigDecimal price;
    private String description;
    private Integer maxPurchasePerAccount;

    public UploadItemProductForm() {
    }

    public UploadItemProductForm(String name, int quantity, BigDecimal price, String description) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxPurchasePerAccount() {
        return maxPurchasePerAccount;
    }

    public void setMaxPurchasePerAccount(Integer maxPurchasePerAccount) {
        this.maxPurchasePerAccount = maxPurchasePerAccount;
    }
}
