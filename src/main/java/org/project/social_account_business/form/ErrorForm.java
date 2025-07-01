package org.project.social_account_business.form;

import lombok.*;

@Setter
@Getter
@Builder(toBuilder = true)
public class ErrorForm {
    private String field;
    private String message;
    public ErrorForm(String field, String message) {
        this.field = field;
        this.message = message;
    }
    public ErrorForm() {
    }

}
