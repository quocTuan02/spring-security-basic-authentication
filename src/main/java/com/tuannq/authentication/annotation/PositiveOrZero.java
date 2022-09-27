package com.tuannq.authentication.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveOrZeroValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveOrZero {
    String message() default "positive-or-zero.invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
