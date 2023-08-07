package com.softserve.itacademy.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskTests {

    private static State validState;
    private static ToDo validToDo;

    @BeforeEach
    void init() {
        validState = new State();
        validState.setName("Valid-State");

        validToDo = new ToDo();
        validToDo.setId(1L);

    }

    @Test
    void constraintViolationOnEmptyTaskName() {
        Task emptyTask = new Task();
        emptyTask.setName("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> violations = validator.validate(emptyTask);

        assertEquals(2, violations.size());
    }

    @Test
    void createValidTask() {
        Task validTask = new Task();
        validTask.setName("Valid-Task");
        validTask.setPriority(Priority.LOW);
        validTask.setState(validState);
        validTask.setTodo(validToDo);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> violations = validator.validate(validTask);
        violations.forEach(System.out::println);

        assertEquals(0, violations.size());
    }

}