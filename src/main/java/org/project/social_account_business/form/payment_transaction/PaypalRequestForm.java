package org.project.social_account_business.form.payment_transaction;

import lombok.Data;

@Data
public class PaypalRequestForm {
    private long userId;
    private double amount;
    private String currency;
    private String description;
}
