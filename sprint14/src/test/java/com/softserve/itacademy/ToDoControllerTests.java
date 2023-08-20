package com.softserve.itacademy;


import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ToDoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoService todoService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getCreateToDoWithValidUserTest() throws Exception {
        long ownerId = 4L;
        mockMvc.perform(get("/todos/create/users/{owner_id}", ownerId))
                .andExpect(status().isOk())
                .andExpect(view().name("create-todo"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attribute("todo", new ToDo()))
                .andExpect(model().attributeExists("ownerId"))
                .andExpect(model().attribute("ownerId", ownerId));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void postCreateToDoWithValidUserTest() throws Exception {
        long ownerId = 4L;
        mockMvc.perform(post("/todos/create/users/{owner_id}", ownerId)
                        .param("title", "ToDo#133"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/all/users/" + ownerId));

        int expectedSize = 8;
        int actualSize = todoService.getAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void postCreateToDoWithInvalidUserTest() throws Exception {
        long ownerId = 100L;
        mockMvc.perform(post("/todos/create/users/{owner_id}", ownerId)
                        .param("title", "ToDo#133"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("User with id 100 not found")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void postCreateToDoWithInvalidDataTest() throws Exception {
        long ownerId = 100L;
        mockMvc.perform(post("/todos/create/users/{owner_id}", ownerId)
                        .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("create-todo"))
                .andExpect(content().string(containsString("The title cannot be empty")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getReadToDoByValidIdTest() throws Exception {
        long todoId = 7L;
        ToDo todo = todoService.readById(todoId);
        List<Task> tasks = taskService.getByTodoId(todoId);
        List<User> users = userService.getAll().stream()
                .filter(user -> user.getId() != todo.getOwner().getId()).collect(Collectors.toList());

        mockMvc.perform(get("/todos/{id}/tasks", todoId))
                .andExpect(status().isOk())
                .andExpect(view().name("todo-tasks"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attribute("todo", todo))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("tasks", tasks))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", users));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getReadToDoByInvalidIdTest() throws Exception {
        long todoId = 1L;
        mockMvc.perform(get("/todos/{id}/tasks", todoId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getUpdateToDoWithValidUserAndToDoTest() throws Exception {
        long ownerId = 4L;
        long toDoId = 7L;

        ToDo todo = todoService.readById(toDoId);

        mockMvc.perform(get("/todos/{todo_id}/update/users/{owner_id}", toDoId, ownerId))
                .andExpect(status().isOk())
                .andExpect(view().name("update-todo"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attribute("todo", todo));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getUpdateToDoWithInvalidTodoIdAndUserIdTest() throws Exception {
        long ownerId = 1L;
        long toDoId = 1L;
        mockMvc.perform(get("/todos/{todo_id}/update/users/{owner_id}", toDoId, ownerId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void postUpdateToDoWithValidUserTest() throws Exception {
        long ownerId = 4L;
        long toDoId = 7L;
        LocalDateTime ldt = LocalDateTime.of(2023, Month.FEBRUARY, 20, 10, 22, 34);

        ToDo todo = todoService.readById(toDoId);
        todo.setTitle("New Title");
        todo.setCreatedAt(ldt);

        mockMvc.perform(post("/todos/{todo_id}/update/users/{owner_id}", toDoId, ownerId)
                        .param("id", String.valueOf(todo.getId()))
                        .param("title", "New Title")
                        .param("createdAt", String.valueOf(ldt)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/all/users/" + ownerId));

        assertThat(todo).isEqualTo(todoService.readById(toDoId));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void postUpdateToDoWithInvalidUserTest() throws Exception {
        long ownerId = 100L;
        mockMvc.perform(post("/todos/create/users/{owner_id}", ownerId)
                        .param("title", "ToDo#133"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("User with id 100 not found")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void postUpdateToDoWithInvalidDataTest() throws Exception {
        long ownerId = 100L;
        mockMvc.perform(post("/todos/create/users/{owner_id}", ownerId)
                        .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("create-todo"))
                .andExpect(content().string(containsString("The title cannot be empty")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getDeleteTodoByValidId() throws Exception {
        long todoId = 7L;
        long ownerId = 4L;
        mockMvc.perform(get("/todos/{todo_id}/delete/users/{owner_id}", todoId, ownerId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/all/users/" + ownerId));

        int expectedSize = 6;
        int actualSize = todoService.getAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getDeleteToDoWithInvalidTodoIdTest() throws Exception {
        long todoId = 100L;
        long ownerId = 4;
        mockMvc.perform(get("/todos/{todo_id}/delete/users/{owner_id}", todoId, ownerId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("To-Do with id 100 not found")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getGetAllTestByValidUserId() throws Exception {
        long ownerId = 4;
        List<ToDo> todos = todoService.getByUserId(ownerId);

        mockMvc.perform(get("/todos/all/users/{user_id}", ownerId))
                .andExpect(status().isOk())
                .andExpect(view().name("todos-user"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attribute("todos", todos))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userService.readById(ownerId)));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getGetAllTestByInvalidUserId() throws Exception {
        long ownerId = 4;
        mockMvc.perform(get("/todos/all/users/{user_id}", ownerId))
                .andExpect(status().isOk())
                .andExpect(view().name("todos-user"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Transactional
    void getAddCollaboratorByValidTodoId() throws Exception {
        long todoId = 7L;
        long collaboratorId = 5L;
        mockMvc.perform(get("/todos/{id}/add", todoId).param("user_id", String.valueOf(collaboratorId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/" + todoId + "/tasks"));

        ToDo toDo = todoService.readById(todoId);
        List<User> collaborators = toDo.getCollaborators();
        User collaborator = userService.readById(collaboratorId);
        assertTrue(collaborators.contains(collaborator));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAddCollaboratorByValidTodoIdWithInvalidCollaboratorId() throws Exception {
        long todoId = 7L;
        long collaboratorId = 15L;
        mockMvc.perform(get("/todos/{id}/add", todoId).param("user_id", String.valueOf(collaboratorId)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("User with id 15 not found")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAddCollaboratorByInvalidTodoId() throws Exception {
        long todoId = 17L;
        long collaboratorId = 7L;
        mockMvc.perform(get("/todos/{id}/add", todoId)
                        .param("user_id", String.valueOf(collaboratorId)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("To-Do with id 17 not found")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Transactional
    void getRemoveCollaboratorByValid() throws Exception {
        long todoId = 7L;
        long collaboratorId = 6L;
        mockMvc.perform(get("/todos/{id}/remove", todoId).param("user_id", String.valueOf(collaboratorId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/" + todoId + "/tasks"));

        ToDo toDo = todoService.readById(todoId);
        List<User> collaborators = toDo.getCollaborators();
        User collaborator = userService.readById(collaboratorId);
        assertFalse(collaborators.contains(collaborator));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getRemoveCollaboratorByValidTodoIdWithInvalidCollaboratorId() throws Exception {
        long todoId = 7L;
        long collaboratorId = 15L;
        mockMvc.perform(get("/todos/{id}/remove", todoId).param("user_id", String.valueOf(collaboratorId)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("User with id 15 not found")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getRemoveCollaboratorByInvalidTodoId() throws Exception {
        long todoId = 17L;
        long collaboratorId = 7L;
        mockMvc.perform(get("/todos/{id}/remove", todoId)
                        .param("user_id", String.valueOf(collaboratorId)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("To-Do with id 17 not found")));
    }




}
