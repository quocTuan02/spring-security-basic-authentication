package com.tuannq.authentication.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LongValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Long {
    String message() default "number.invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
