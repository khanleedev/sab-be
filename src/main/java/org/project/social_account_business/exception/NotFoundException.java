package org.project.social_account_business.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private String code;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
