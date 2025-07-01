package org.project.social_account_business.validation;

import org.project.social_account_business.service.id.IdGenerator;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@IdGeneratorType( IdGenerator.class)
@Retention( RetentionPolicy.RUNTIME)
@Target({ FIELD, METHOD})
public @interface SystemId {
    String message() default "Invalid system id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
