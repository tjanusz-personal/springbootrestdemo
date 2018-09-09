package com.demo.springbootrestdemo.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Spring validator to validate string values are valid UUID for our system. This class is used in conjunction with
 * the annotation to provide simple field level UUID validations.
 */
public class UUIDValidator implements ConstraintValidator<UUID, String> {

  public static final String REGEX_UUID = "\\p{XDigit}{8}(-\\p{XDigit}{4}){4}\\p{XDigit}{8}";

  @Override
  public void initialize(UUID constraintAnnotation) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // This allows null values to allow for optional UUIDs. If a UUID field is required,
    // it should also be annotated with @NotNull
    return value == null || isUUID(value);
  }

  public static boolean isUUID(String value) {
    return value != null && value.matches(REGEX_UUID);
  }
}
