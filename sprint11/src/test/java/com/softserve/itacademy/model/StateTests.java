package com.softserve.itacademy.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StateTests {

    @Test
    void constraintViolationOnEmptyStateName() {
        State emptyState = new State();
        emptyState.setName("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(emptyState);

        for (ConstraintViolation<State> violation : violations) {
            System.out.println("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
        }

        assertEquals(2, violations.size());
    }

    @Test
    void constraintViolationOnInvalidStateName() {
        State invalidState = new State();
        invalidState.setName("Invalid$Name");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(invalidState);

        assertEquals(1, violations.size());
    }

    @Test
    void createValidState() {
        State validState = new State();
        validState.setName("Valid-State");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(validState);

        assertEquals(0, violations.size());
    }

    @Test
    void constraintViolationOnMaxStateNameLength() {
        State longState = new State();
        longState.setName("ThisIsALongStateNameThatExceedsTwentyCharacters");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(longState);

        assertEquals(1, violations.size());
    }

    @Test
    void constraintViolationOnInvalidCharactersInStateName() {
        State invalidCharactersState = new State();
        invalidCharactersState.setName("Invalid_@Chars");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(invalidCharactersState);

        assertEquals(1, violations.size());
    }
}