package org.project.social_account_business.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.Serial;

/**
 * Exception thrown when a currency is not registered in the system.
 */
@Getter
@Setter
public class UnregisteredCurrencyException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public UnregisteredCurrencyException(String message) {
        super(message);
    }

    public UnregisteredCurrencyException(String message, String code) {
        super(message);
        this.message = code;
    }

}
