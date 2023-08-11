package com.softserve.itacademy.service;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ToDoServiceTests {

    ToDoService toDoService;

    private ToDo createSampleToDo() {
        ToDo toDo = new ToDo();
        toDo.setTitle("Sample Title");
        return toDo;
    }

    @Autowired
    public ToDoServiceTests(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @Test
    @Transactional
    public void createToDoTest() {
        ToDo toDo = createSampleToDo();
        assertThrows(javax.persistence.EntityNotFoundException.class, () -> toDoService.create(null));
    }

    @Test
    @Transactional
    public void updateToDoTest() {
        ToDo expected = toDoService.readById(7L);
        expected.setTitle("New Title");
        ToDo actual = toDoService.update(expected);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void deleteToDoTest() {
        int expected = toDoService.getAll().size();
        toDoService.delete(7L);
        int actual = toDoService.getAll().size();
        assertNotEquals(expected, actual);
    }

    @Test
    public void getAllToDosTest() {
        List<ToDo> toDoList = toDoService.getAll();
        assertNotNull(toDoList);
    }

    @Test
    public void getByUserIdTest() {
        assertThrows(javax.persistence.EntityNotFoundException.class, () -> toDoService.getByUserId(-1L));
    }

    @Test
    public void readByIdTest() {
        assertThrows(javax.persistence.EntityNotFoundException.class, () -> toDoService.readById(-1L));
    }
}

