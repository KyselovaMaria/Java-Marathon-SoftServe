package com.softserve.itacademy;


import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.impl.ToDoServiceImpl;
import com.softserve.itacademy.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(JUnitPlatform.class)
public class ToDoServiceTest {
    private static UserService userService;
    private static ToDoService toDoService;

    @BeforeAll
    public static void setupBeforeClass() throws Exception {
        AnnotationConfigApplicationContext annotationConfigContext = new AnnotationConfigApplicationContext(Config.class);
        userService = annotationConfigContext.getBean(UserService.class);
        toDoService = annotationConfigContext.getBean(ToDoService.class);
        annotationConfigContext.close();
    }

    @AfterEach
    public void clearBeforeNext() {
        userService.getAll().forEach(user -> user.getMyTodos().clear());
        userService.getAll().clear();
        toDoService.getAll().forEach(todo -> todo.getTasks().clear());
    }

    @Test
    public void checkAddToDo() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo1 = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        ToDo actual = toDoService.addTodo(toDo1, user);

        Assertions.assertEquals(toDo1, actual);

        Assertions.assertThrows(IllegalArgumentException.class, () -> toDoService.addTodo(null, user));
        Assertions.assertThrows(IllegalArgumentException.class, () -> toDoService.addTodo(toDo1, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> toDoService.addTodo(null, null));
    }

    @Test
    public void checkUpdateToDo() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);

        Task task1 = new Task("Task#1", Priority.MEDIUM);

        ToDo toDo11 = new ToDo("Todo#1", LocalDateTime.now(), null, Arrays.asList(task1));
        toDoService.addTodo(toDo11, user);

        Assertions.assertThrows(IllegalArgumentException.class, () -> toDoService.updateTodo(null));

        ToDo newTodDo = new ToDo("Todo#1", LocalDateTime.of(2023,8,1,12,55), user, Arrays.asList(task1));

        Assertions.assertEquals(toDo11,toDoService.updateTodo(newTodDo));
        Assertions.assertEquals(1,toDoService.getByUserTitle(user,"Todo#1").getTasks().size());
    }

    @Test
    public void checkDeleteToDo() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);

        Task task1 = new Task("Task#1", Priority.MEDIUM);
        Task task2 = new Task("Task#2", Priority.MEDIUM);
        Task task3 = new Task("Task#3", Priority.HIGH);
        Task task4 = new Task("Task#4", Priority.LOW);

        ToDo toDo11 = new ToDo("Todo#1", LocalDateTime.now(), null, Arrays.asList(task1, task2));
        ToDo toDo12 = new ToDo("Todo#2", LocalDateTime.now(), null, Arrays.asList(task4, task3));

        toDoService.addTodo(toDo11, user);
        toDoService.addTodo(toDo12, user);

        toDoService.deleteTodo(toDo11);
        toDoService.deleteTodo(toDo12);

        Assertions.assertEquals(0,toDoService.getByUser(user).size());
    }

    @Test
    public void checkGetAll() {
        User user1 = new User("Anna", "An", "anna@gmail.com", "anna");
        User user2 = new User("Julia", "Jul", "julia@gmail.com", "julia");
        User user3 = new User("Olena", "Ol", "olena@gmail.com", "olena");

        Task task1 = new Task("Task#1", Priority.MEDIUM);
        Task task2 = new Task("Task#2", Priority.MEDIUM);

        Task task3 = new Task("Task#3", Priority.HIGH);
        Task task4 = new Task("Task#4", Priority.LOW);

        Task task5 = new Task("Task#5", Priority.HIGH);
        Task task6 = new Task("Task#6", Priority.LOW);

        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);

        ToDo toDo11 = new ToDo("Todo#1", LocalDateTime.now(), null, Arrays.asList(task1, task2));
        ToDo toDo21 = new ToDo("Todo#2", LocalDateTime.now(), null, Arrays.asList(task3, task4));
        ToDo toDo31 = new ToDo("Todo#3", LocalDateTime.now(), null, Arrays.asList(task5, task6));

        toDoService.addTodo(toDo11, user1);
        toDoService.addTodo(toDo21, user2);
        toDoService.addTodo(toDo31, user3);

        Assertions.assertEquals(3,toDoService.getAll().size());

        userService.deleteUser(user1);
        userService.deleteUser(user2);
        userService.deleteUser(user3);
    }

    @Test
    public void checkGetByUser() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);

        Task task1 = new Task("Task#1", Priority.MEDIUM);
        Task task2 = new Task("Task#2", Priority.MEDIUM);
        Task task3 = new Task("Task#3", Priority.HIGH);

        Task task4 = new Task("Task#1", Priority.LOW);
        Task task5 = new Task("Task#2", Priority.HIGH);
        Task task6 = new Task("Task#3", Priority.LOW);

        ToDo toDo11 = new ToDo("Todo#1", LocalDateTime.now(), null, Arrays.asList(task1, task2, task3));
        ToDo toDo12 = new ToDo("Todo#2", LocalDateTime.now(), null, Arrays.asList(task4, task5, task6));

        toDoService.addTodo(toDo11, user);
        toDoService.addTodo(toDo12, user);

        Assertions.assertThrows(IllegalArgumentException.class, () -> toDoService.getByUser(null));
        Assertions.assertEquals(Arrays.asList(toDo11, toDo12), toDoService.getByUser(user));

        userService.deleteUser(user);
    }

    @Test
    public void checkGetByUserTitle() {
        User user1 = new User("Anna", "An", "anna@gmail.com", "anna");
        User user2 = new User("Julia", "Jul", "julia@gmail.com", "julia");
        User user3 = new User("Olena", "Ol", "olena@gmail.com", "olena");

        Task task1 = new Task("Task#1", Priority.MEDIUM);
        Task task2 = new Task("Task#2", Priority.MEDIUM);
        Task task3 = new Task("Task#3", Priority.HIGH);
        Task task4 = new Task("Task#4", Priority.LOW);
        Task task5 = new Task("Task#5", Priority.HIGH);
        Task task6 = new Task("Task#6", Priority.LOW);

        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);

        ToDo toDo11 = new ToDo("Todo#1", LocalDateTime.now(), null, Arrays.asList(task1, task2));
        ToDo toDo21 = new ToDo("Todo#2", LocalDateTime.now(), null, Arrays.asList(task3, task4));
        ToDo toDo31 = new ToDo("Todo#3", LocalDateTime.now(), null, Arrays.asList(task5, task6));

        toDoService.addTodo(toDo11, user1);
        toDoService.addTodo(toDo21, user2);
        toDoService.addTodo(toDo31, user3);

        Assertions.assertEquals(toDo11, toDoService.getByUserTitle(user1, "Todo#1"));
        Assertions.assertEquals(toDo21, toDoService.getByUserTitle(user2, "Todo#2"));
        Assertions.assertEquals(toDo31, toDoService.getByUserTitle(user3, "Todo#3"));

        Assertions.assertThrows(IllegalArgumentException.class, ()-> toDoService.getByUserTitle(null, "Todo#1"));
    }
}