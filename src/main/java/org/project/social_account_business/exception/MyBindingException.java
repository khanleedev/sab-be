package org.project.social_account_business.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.project.social_account_business.form.ErrorForm;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyBindingException extends RuntimeException {
    private final String errors;

    public MyBindingException(String errors) {
        super("Validation failed");
        this.errors = errors;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getErrors() {
        return errors;
    }
}
