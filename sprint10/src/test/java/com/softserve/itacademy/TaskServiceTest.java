package com.softserve.itacademy;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.impl.TaskServiceImpl;
import com.softserve.itacademy.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitPlatform.class)
public class TaskServiceTest {
    private static UserService userService;
    private static ToDoService toDoService;
    private static TaskService taskService;

    @BeforeAll
    public static void setupBeforeClass() throws Exception {
        AnnotationConfigApplicationContext annotationConfigContext = new AnnotationConfigApplicationContext(Config.class);
        userService = annotationConfigContext.getBean(UserService.class);
        toDoService = annotationConfigContext.getBean(ToDoService.class);
        taskService = annotationConfigContext.getBean(TaskService.class);
        annotationConfigContext.close();
    }

    @AfterEach
    public void clearBeforeNext() {
        userService.getAll().forEach(user -> user.getMyTodos().clear());
        userService.getAll().clear();
        toDoService.getAll().forEach(todo -> todo.getTasks().clear());
    }

    @Test
    public void checkAddTask() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);

        Task task1 = new Task("Task #1", Priority.HIGH);
        Task actual = taskService.addTask(task1, toDo);
        Assertions.assertEquals(task1, actual);
    }

    @Test
    public void checkAddTaskWithNullToDo() {
        Task task1 = new Task("Task #1", Priority.HIGH);
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.addTask(task1, null));
        Assertions.assertEquals("ToDo cannot be null!", exception.getMessage());
    }

    @Test
    public void checkAddTaskWithNullTask() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.addTask(null, toDo));
        Assertions.assertEquals("Task cannot be null!", exception.getMessage());
    }

    @Test
    public void checkAddTaskWithNotExistingToDo() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        Task task1 = new Task("Task #1", Priority.HIGH);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.addTask(task1, toDo));
        Assertions.assertEquals("There is no " + toDo.getTitle() + " exist!", exception.getMessage());
    }

    @Test
    public void checkAddTaskWithExistingTask() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo1 = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        ToDo toDo2 = new ToDo("ToDo #2", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo1, user);
        toDoService.addTodo(toDo2, user);

        Task task1 = new Task("Task #1", Priority.HIGH);
        Task actual = taskService.addTask(task1, toDo1);
        Assertions.assertEquals(task1, actual);

        Task task2 = new Task("Task #1", Priority.HIGH);
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.addTask(task2, toDo2));
        Assertions.assertEquals("Task " + task2.getName() + " already exist!", exception.getMessage());
    }

    @Test
    public void checkUpdateTask() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        Task task1 = new Task("Task #1", Priority.HIGH);
        Task task2 = new Task("Task #1", Priority.MEDIUM);
        taskService.addTask(task1, toDo);

        Task actual = taskService.updateTask(task2);
        Assertions.assertEquals(task2, actual);
    }

    @Test
    public void checkUpdateTaskWithNullTask() {
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.updateTask(null));
        Assertions.assertEquals("Task cannot be null!", exception.getMessage());
    }

    @Test
    public void checkUpdateTaskWithNotExistingTask() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        Task task1 = new Task("Task #1", Priority.HIGH);
        Task task2 = new Task("Task #2", Priority.LOW);
        taskService.addTask(task1, toDo);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.updateTask(task2));
        Assertions.assertEquals("Task " + task2.getName() + " is not found!", exception.getMessage());
    }

    @Test
    public void checkDeleteTask() {
        List<Task> expected = new ArrayList<>();
        List<Task> actual;

        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDoService toDoService = new ToDoServiceImpl(userService);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        TaskService taskService = new TaskServiceImpl(toDoService);
        Task task1 = new Task("Task #1", Priority.HIGH);
        Task task2 = new Task("Task #2", Priority.LOW);
        Task task3 = new Task("Task #3", Priority.LOW);

        taskService.addTask(task1, toDo);
        taskService.addTask(task2, toDo);
        taskService.addTask(task3, toDo);
        taskService.deleteTask(task2);

        expected.add(task1);
        expected.add(task3);

        actual = taskService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void checkDeleteTaskWithOneTask() {
        List<Task> expected = new ArrayList<>();
        List<Task> actual;

        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDoService toDoService = new ToDoServiceImpl(userService);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        TaskService taskService = new TaskServiceImpl(toDoService);
        Task task1 = new Task("Task #1", Priority.HIGH);

        taskService.addTask(task1, toDo);
        taskService.deleteTask(task1);

        actual = taskService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void checkDeleteTaskWithNullTask() {
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.deleteTask(null));
        Assertions.assertEquals("Task cannot be null!", exception.getMessage());
    }

    @Test
    public void checkDeleteTaskWithNotExistingTask() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDoService toDoService = new ToDoServiceImpl(userService);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        TaskService taskService = new TaskServiceImpl(toDoService);
        Task task = new Task("Task #1", Priority.HIGH);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.deleteTask(task));
        Assertions.assertEquals("There is no ToDo list with " + task.getName() + " task in it!", exception.getMessage());
    }

    @Test
    public void checkGetByToDoName() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        Task task1 = new Task("Task #1", Priority.HIGH);
        Task task2 = new Task("Task #2", Priority.LOW);
        taskService.addTask(task1, toDo);
        taskService.addTask(task2, toDo);

        Task actual = taskService.getByToDoName(toDo, "Task #1");
        Assertions.assertEquals(task1, actual);
    }

    @Test
    public void checkGetByToDoNameWithNullToDo() {
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByToDoName(null, "Task #1"));
        Assertions.assertEquals("ToDo cannot be null!", exception.getMessage());
    }

    @Test
    public void checkGetByToDoNameWithNullOrEmptyName() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        Task task1 = new Task("Task #1", Priority.HIGH);
        taskService.addTask(task1, toDo);

        IllegalArgumentException exception1 = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByToDoName(toDo, null));
        Assertions.assertEquals("Name of Task cannot be null or empty!", exception1.getMessage());

        IllegalArgumentException exception2 = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByToDoName(toDo, ""));
        Assertions.assertEquals("Name of Task cannot be null or empty!", exception2.getMessage());
    }

    @Test
    public void checkGetByToDoNameWithNotExistingToDo() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByToDoName(toDo, "Task #1"));
        Assertions.assertEquals("There is no " + toDo.getTitle() + " exist!", exception.getMessage());
    }

    @Test
    public void checkGetByToDoNameWithNotExistingTask() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        Task task1 = new Task("Task #1", Priority.HIGH);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByToDoName(toDo, task1.getName()));
        Assertions.assertEquals("Task with name " + task1.getName() + " is not found!", exception.getMessage());
    }

    @Test
    public void checkGetByToDo() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        Task task1 = new Task("Task #1", Priority.HIGH);
        Task task2 = new Task("Task #2", Priority.LOW);

        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);

        taskService.addTask(task1, toDo);
        taskService.addTask(task2, toDo);
        Assertions.assertEquals(expected, taskService.getByToDo(toDo));
    }

    @Test
    public void checkGetByToDoWithNullToDo() {
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByToDo(null));
        Assertions.assertEquals("ToDo cannot be null!", exception.getMessage());
    }

    @Test
    public void checkGetByToDoWithNotExistingToDo() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByToDo(toDo));
        Assertions.assertEquals("There is no " + toDo.getTitle() + " exist!", exception.getMessage());
    }

    @Test
    public void checkGetAll() {
        // Different users
        User user1 = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        User user2 = new User("Emma", "Faris", "emmaFaris@email.com", "password2");
        userService.addUser(user1);
        userService.addUser(user2);

        // Different toDos
        ToDo toDo1 = new ToDo("ToDo #1", LocalDateTime.now(), user1, new ArrayList<>());
        ToDo toDo2 = new ToDo("ToDo #2", LocalDateTime.now(), user2, new ArrayList<>());
        ToDo toDo3 = new ToDo("ToDo #3", LocalDateTime.now(), user2, new ArrayList<>());
        toDoService.addTodo(toDo1, user1);
        toDoService.addTodo(toDo2, user2);
        toDoService.addTodo(toDo3, user2);

        Task task1 = new Task("Task #1", Priority.HIGH);
        Task task2 = new Task("Task #2", Priority.LOW);
        Task task3 = new Task("Task #3", Priority.LOW);
        taskService.addTask(task1, toDo1);
        taskService.addTask(task2, toDo2);
        taskService.addTask(task3, toDo3);

        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        expected.add(task3);

        Assertions.assertEquals(expected, taskService.getAll());
    }

    @Test
    public void checkGetAllWithoutData() {
        User user1 = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user1);
        ToDo toDo1 = new ToDo("ToDo #1", LocalDateTime.now(), user1, new ArrayList<>());
        toDoService.addTodo(toDo1, user1);
        Assertions.assertEquals(new ArrayList<>(), taskService.getAll());
    }

    @Test
    public void checkGetByUserName() {
        // Different users
        User user1 = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        User user2 = new User("Emma", "Faris", "emmaFaris@email.com", "password2");
        userService.addUser(user1);
        userService.addUser(user2);

        // Different toDos
        ToDo toDo1 = new ToDo("ToDo #1", LocalDateTime.now(), user1, new ArrayList<>());
        ToDo toDo2 = new ToDo("ToDo #2", LocalDateTime.now(), user1, new ArrayList<>());
        toDoService.addTodo(toDo1, user1);
        toDoService.addTodo(toDo2, user2);

        Task task1 = new Task("Task #1", Priority.HIGH);
        Task task2 = new Task("Task #2", Priority.LOW);
        Task task3 = new Task("Task #3", Priority.MEDIUM);
        taskService.addTask(task1, toDo1);
        taskService.addTask(task2, toDo2);
        taskService.addTask(task3, toDo2);

        Task actual = taskService.getByUserName(user2, "Task #2");
        Assertions.assertEquals(task2, actual);
    }

    @Test
    public void checkGetByUserNameWithNotExistingName() {
        // Different users
        User user1 = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        User user2 = new User("Emma", "Faris", "emmaFaris@email.com", "password2");
        userService.addUser(user1);
        userService.addUser(user2);

        // Different toDos
        ToDo toDo1 = new ToDo("ToDo #1", LocalDateTime.now(), user1, new ArrayList<>());
        ToDo toDo2 = new ToDo("ToDo #2", LocalDateTime.now(), user1, new ArrayList<>());
        toDoService.addTodo(toDo1, user1);
        toDoService.addTodo(toDo2, user2);

        Task task1 = new Task("Task #1", Priority.HIGH);
        Task task2 = new Task("Task #2", Priority.LOW);
        Task task3 = new Task("Task #3", Priority.MEDIUM);
        taskService.addTask(task1, toDo1);
        taskService.addTask(task2, toDo2);
        taskService.addTask(task3, toDo2);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByUserName(user2, task1.getName()));
        Assertions.assertEquals("Task with name " + task1.getName() + " is not found!", exception.getMessage());
    }

    @Test
    public void checkGetByUserNameWithNullUser() {
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByUserName(null, "Task #1"));
        Assertions.assertEquals("User cannot be null!", exception.getMessage());
    }

    @Test
    public void checkGetByUserNameWithNullOrEmptyName() {
        User user = new User("Anna", "Faris", "annaFaris@email.com", "password1");
        userService.addUser(user);
        ToDo toDo = new ToDo("ToDo #1", LocalDateTime.now(), user, new ArrayList<>());
        toDoService.addTodo(toDo, user);
        Task task1 = new Task("Task #1", Priority.HIGH);
        taskService.addTask(task1, toDo);

        IllegalArgumentException exception1 = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByUserName(user, null));
        Assertions.assertEquals("Name of Task cannot be null or empty!", exception1.getMessage());

        IllegalArgumentException exception2 = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getByUserName(user, ""));
        Assertions.assertEquals("Name of Task cannot be null or empty!", exception2.getMessage());
    }
}
