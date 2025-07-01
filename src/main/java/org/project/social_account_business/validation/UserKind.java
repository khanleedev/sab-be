package org.project.social_account_business.validation;

import jakarta.validation.Constraint;
import org.project.social_account_business.validation.validationImpl.UserKindValidation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserKindValidation.class)
@Documented
public @interface UserKind {
    boolean allowNull() default false;

    String message() default "User kind invalid!";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
