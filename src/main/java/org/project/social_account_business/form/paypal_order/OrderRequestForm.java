package org.project.social_account_business.form.paypal_order;

import lombok.Data;

@Data
public class OrderRequestForm {
    private double amount;
    private String currency;
}
