package com.softserve.itacademy.service;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Sergey Tsynin
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class TaskServiceTests {

    private final TaskService taskService;

    @Autowired
    public TaskServiceTests(TaskService taskService) {
        this.taskService = taskService;
    }

    @Test
    public void getTaskByIdTest() {

        // when
        Task actual = taskService.readById(5L);

        // then
        assertEquals("Task #1", actual.getName());
        assertEquals(Priority.HIGH, actual.getPriority());
    }

    @Test
    @Transactional
    public void createTaskTest() {

        // given
        Task task = new Task();
        task.setName("New Task");

        // when
        Task newTask = taskService.create(task);

        // then
        assertNotEquals(0, newTask.getId());
    }

    @Test
    @Transactional
    public void updateTaskTest() {

        // given
        Task task = taskService.readById(5L);
        task.setName("New Name");

        // when
        Task newTask = taskService.update(task);

        // then
        assertEquals(5L, newTask.getId());
        assertEquals("New Name", newTask.getName());
    }

    @Test
    @Transactional
    public void getAllTaskTest() {

        // given
        taskService.delete(5L);
        taskService.delete(6L);
        taskService.delete(7L);

        //when
        List<Task> tasks = taskService.getAll();

        //then
        assertEquals(0, tasks.size());
    }

    @Test
    @Transactional
    public void getAllTaskByTodoIdTest() {

        //when
        List<Task> tasks = taskService.getByTodoId(7);

        //then
        assertEquals(3, tasks.size());
    }
}
