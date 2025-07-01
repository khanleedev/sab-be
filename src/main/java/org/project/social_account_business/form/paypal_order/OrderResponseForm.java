package org.project.social_account_business.form.paypal_order;

import lombok.Data;

@Data
public class OrderResponseForm {
    private String orderId;
    private String approvalUrl;
}
