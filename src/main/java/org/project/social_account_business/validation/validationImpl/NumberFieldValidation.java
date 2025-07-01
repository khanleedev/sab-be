package org.project.social_account_business.validation.validationImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;
import org.project.social_account_business.validation.NumberField;

@Log4j2
public class NumberFieldValidation implements ConstraintValidator<NumberField, Object> {
    private boolean allowNull;

    @Override
    public void initialize(NumberField constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        if (value instanceof Integer) {
            return (Integer) value >= 0;
        } else if (value instanceof String) {
            try {
                Integer intValue = Integer.parseInt((String) value);
                return intValue >= 0;
            } catch (NumberFormatException e) {
                log.error("NumberFieldValidation.isValid error", e);
                return false;
            }
        }
        return false;
    }
}