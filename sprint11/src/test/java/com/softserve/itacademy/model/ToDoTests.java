package com.softserve.itacademy.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ToDoTests {

    private static User validUser;
    private static ToDo validToDo;

    @BeforeAll
    static void init() {
        Role userRole = new Role();
        userRole.setName("USER");
        validUser = new User();
        validUser.setEmail("valid@cv.edu.ua");
        validUser.setFirstName("Valid-Name");
        validUser.setLastName("Valid-Name");
        validUser.setPassword("qwQW12!@");
        validUser.setRole(userRole);

        validToDo = new ToDo();
        validToDo.setTitle("Valid ToDo Title");
        validToDo.setCreatedAt(LocalDateTime.now());
        validToDo.setOwner(validUser);
    }


    @Test
    void createValidToDo() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(validToDo);

        assertEquals(0, violations.size());
    }


    @Test
    void toDoWithEmptyTitle() {
        ToDo toDo = new ToDo();
        toDo.setTitle("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(toDo);

        assertEquals(1, violations.size());
        assertEquals("Title cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void toDoWithLongTitle() {
        ToDo toDo = new ToDo();
        toDo.setTitle("This is very long title created special to test title constraint. This title contains more than 256 characters and should be validated as incorrected. " +
                "This is very long title created special to test title constraint. This title contains more than 256 characters and should be validated as incorrected. ");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(toDo);

        assertEquals(1, violations.size());
        assertEquals("Title length must be between 1 and 255 characters", violations.iterator().next().getMessage());
    }

    @Test
    void createdAtNotNull() {
        assertNotNull(validToDo.getCreatedAt());
    }

    @Test
    void ownerNotNull() {
        assertNotNull(validToDo.getOwner());
    }

    @Test
    void collaboratorsNotNull() {
        Role collboratorRole = new Role();
        collboratorRole.setName("COLLABORATOR");
        User collaborator = new User();
        collaborator.setEmail("valid@cv.edu.ua");
        collaborator.setFirstName("Valid-Name");
        collaborator.setLastName("Valid-Name");
        collaborator.setPassword("qwQW12!@");
        collaborator.setRole(collboratorRole);

        List<User> collaborators = new ArrayList<>();
        collaborators.add(collaborator);
        validToDo.setCollaborators(collaborators);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(validToDo);

        assertEquals(0, violations.size());
        assertEquals(1, validToDo.getCollaborators().size());
    }

    @Test
    void setId() {
        Long id = 1L;
        validToDo.setId(id);
        assertEquals(id, validToDo.getId());
    }

    @Test
    void setTitle() {
        String newTitle = "New title";
        validToDo.setTitle(newTitle);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(validToDo);

        assertEquals(0, violations.size());
        assertEquals(newTitle, validToDo.getTitle());
    }

}

