package com.demo.springbootrestdemo.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Sample Annotation for our UUID validations we use on certain key values. This was so common in our code we used
 * annotations.
 */
@Documented
@Constraint(validatedBy = UUIDValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface UUID {
  String message() default "Invalid UUID: ${validatedValue}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
