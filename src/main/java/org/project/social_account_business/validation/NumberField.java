package org.project.social_account_business.validation;


import jakarta.validation.Constraint;
import org.project.social_account_business.validation.validationImpl.NumberFieldValidation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumberFieldValidation.class)
@Documented
public @interface NumberField {
    boolean allowNull() default false;

    String message() default "Number field invalid!";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
