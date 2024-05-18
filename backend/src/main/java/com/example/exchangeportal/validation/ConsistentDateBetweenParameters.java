package com.example.exchangeportal.validation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ConsistentDateBetweenParameterValidator.class)
@Target({ METHOD, CONSTRUCTOR })
@Retention(RUNTIME)
@Documented
public @interface ConsistentDateBetweenParameters {

    String message() default "End date must be after start date and both must be in the past or both must be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int fromDatePosition() default 1;

    int toDatePosition() default 2;
}
