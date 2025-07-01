package org.project.social_account_business.validation.validationImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.validation.UserKind;

public class UserKindValidation implements ConstraintValidator<UserKind, Integer> {
    @Override
    public void initialize(UserKind userKind) {
        boolean allowNull = userKind.allowNull();
    }

    //    private boolean allowNull;
//    @Override
//    public void initialize(UserKind constraintAnnotation) {
//        allowNull = constraintAnnotation.allowNull();
//    }
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == BetaConstant.USER_KIND_ADMIN
                || value == BetaConstant.USER_KIND_USER;
    }
}