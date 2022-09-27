package com.tuannq.authentication.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Positive {
    String message() default "positive.invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
