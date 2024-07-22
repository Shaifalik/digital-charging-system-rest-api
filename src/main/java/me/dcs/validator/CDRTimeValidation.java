package me.dcs.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static me.dcs.utils.ApplicationConstants.INCORRECT_TIME;

@Constraint(validatedBy = CDRTimeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CDRTimeValidation {
    String message() default INCORRECT_TIME;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

