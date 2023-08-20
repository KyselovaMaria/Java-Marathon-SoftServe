package com.softserve.itacademy;

import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoService toDoService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private StateService stateService;

    @Test
    public void getCreateTaskWithValidTodoTest() throws Exception {
        long toDoId = 7L;

        mockMvc.perform(get("/tasks/create/todos/{todo_id}", toDoId))
                .andExpect(status().isOk())
                .andExpect(view().name("create-task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("task", new TaskDto()))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attribute("todo", toDoService.readById(toDoId)));
    }

    @Test
    public void getCreateTaskWithInvalidTodoTest() throws Exception {
        long toDoId = 1L;
        mockMvc.perform(get("/tasks/create/todos/{todo_id}", toDoId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    public void postCreateTaskWithValidDataTest() throws Exception {
        long toDoId = 7L;

        mockMvc.perform(post("/tasks/create/todos/{todo_id}", toDoId)
                        .param("name", "Task #4")
                        .param("priority", "MEDIUM")
                        .param("todoId", String.valueOf(toDoId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/" + toDoId + "/tasks"));
    }

    @Test
    public void postCreateTaskWithInvalidDataTest() throws Exception {
        long toDoId = 7L;

        mockMvc.perform(post("/tasks/create/todos/{todo_id}", toDoId)
                        .param("name", "")
                        .param("priority", "MEDIUM")
                        .param("todoId", String.valueOf(toDoId)))
                .andExpect(status().isOk())
                .andExpect(view().name("create-task"))
                .andExpect(content().string(containsString("The name cannot be empty")));
    }

    @Test
    public void getUpdateTaskWithValidTaskId() throws Exception {
        long toDoId = 7L;
        long taskId = 7L;

        mockMvc.perform(get("/tasks/{task_id}/update/todos/{todo_id}", taskId, toDoId))
                .andExpect(status().isOk())
                .andExpect(view().name("update-task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("task", TaskTransformer.convertToDto(taskService.readById(taskId))));
    }

    @Test
    public void getUpdateTaskWithInvalidTaskId() throws Exception {
        long toDoId = 7L;
        long taskId = 10L;

        mockMvc.perform(get("/tasks/{task_id}/update/todos/{todo_id}", taskId, toDoId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    public void postUpdateTaskWithValidData() throws Exception {
        long toDoId = 7L;
        long taskId = 7L;
        Task newTask = taskService.readById(taskId);
        newTask.setName("Task #5");
        newTask.setPriority(Priority.HIGH);
        newTask.setState(stateService.readById(7));

        mockMvc
                .perform(post("/tasks/{task_id}/update/todos/{todo_id}", taskId, toDoId)
                        .param("id", String.valueOf(taskId))
                        .param("name", "Task #5")
                        .param("priority", "HIGH")
                        .param("stateId", "7")
                        .param("todoId", String.valueOf(toDoId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/" + toDoId + "/tasks"));

        assertThat(newTask).isEqualTo(taskService.readById(taskId));
    }

    @Test
    public void postUpdateTaskWithInvalidData() throws Exception {
        long toDoId = 7L;
        long taskId = 7L;

        mockMvc
                .perform(post("/tasks/{task_id}/update/todos/{todo_id}", taskId, toDoId)
                        .param("id", String.valueOf(taskId))
                        .param("name", "")
                        .param("priority", "HIGH")
                        .param("stateId", "7")
                        .param("todoId", String.valueOf(toDoId)))
                .andExpect(status().isOk())
                .andExpect(view().name("update-task"))
                .andExpect(content().string(containsString("The name cannot be empty")));

        mockMvc
                .perform(post("/tasks/{task_id}/update/todos/{todo_id}", taskId, toDoId)
                        .param("id", String.valueOf(taskId))
                        .param("name", "Task #4")
                        .param("priority", "HIGH")
                        .param("stateId", "2")
                        .param("todoId", String.valueOf(toDoId)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    public void getDeleteTaskWithValidTaskId() throws Exception {
        long toDoId = 7L;
        long taskId = 5L;
        Task deletedTask = taskService.readById(taskId);

        mockMvc.perform(get("/tasks/{task_id}/delete/todos/{todo_id}", taskId, toDoId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/" + toDoId + "/tasks"));

        assertFalse(taskService.getByTodoId(toDoId).contains(deletedTask));
    }

    @Test
    public void getDeleteTaskWithInvalidTaskId() throws Exception {
        long toDoId = 7L;
        long taskId = 10L;

        mockMvc.perform(get("/tasks/{task_id}/delete/todos/{todo_id}", taskId, toDoId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }
}
