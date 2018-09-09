package com.demo.springbootrestdemo.domain.validation;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UUIDValidatorTest {
    private UUIDValidator validator = new UUIDValidator();

    @Test
    public void affirmsAValidUUID() {
        assertTrue(validator.isValid("12345678-1234-5678-9012-091872345612", null));
    }

    @Test
    public void failsAUUIDWithTooManyCharacters() {
        assertFalse(validator.isValid("12345678-1234-5678-9012-1234567890123", null));
    }

    @Test
    public void failsAUUIDWithImproperGrouping() {
        assertFalse(validator.isValid("12345678-1234-567-89012-123456789012", null));
    }

    @Test
    public void failsARandomString() {
        assertFalse(validator.isValid("ajicyq24089t3piaghviuerongcao34inuvap34cgha934", null));
    }

    @Test
    public void failsAnEmptyString() {
        assertFalse(validator.isValid("", null));
    }

    @Test
    public void affirmsANullString() {
        assertTrue(validator.isValid(null, null));
    }

}