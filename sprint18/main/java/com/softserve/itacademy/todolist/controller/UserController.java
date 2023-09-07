package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.model.*;
import com.softserve.itacademy.todolist.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final ToDoService toDoService;
    private final TaskService taskService;
    private final StateService stateService;
    private final RoleService roleService;

    public UserController(UserService userService, ToDoService toDoService, TaskService taskService,
                          StateService stateService, RoleService roleService) {
        this.userService = userService;
        this.toDoService = toDoService;
        this.taskService = taskService;
        this.stateService = stateService;
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<UserResponse> getAll() {
        return userService.getAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    UserResponse createUser(@Valid @RequestBody User newUser) {
        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        user.setRole(roleService.readById(2));
        return new UserResponse(userService.create(user));
    }

    @GetMapping("/{u_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or authentication.principal.id == #u_id")
    UserResponse getUser(@PathVariable Long u_id) {
        User user = userService.readById(u_id);
        if (user == null)
            throw new EntityNotFoundException();
        return new UserResponse(user);
    }

    @PutMapping("/{u_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or authentication.principal.id == #u_id")
    UserResponse updateUser(@PathVariable Long u_id, @Valid @RequestBody User newUser) {
        User user = userService.readById(u_id);
        if (user == null)
            throw new EntityNotFoundException();
        Role role = roleService.readById(2);
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        user.setRole(role);
        User updatedUser = userService.update(user);
        return new UserResponse(updatedUser);
    }

    @DeleteMapping("/{u_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    UserResponse deleteUser(@PathVariable Long u_id) {
        User user = userService.readById(u_id);
        if (user == null)
            throw new EntityNotFoundException();
        userService.delete(u_id);
        return new UserResponse(user);
    }

    @GetMapping("/{u_id}/todos")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or authentication.principal.id == #u_id")
    List<ToDo> getTodosByUser(@PathVariable Long u_id) {
        User user = userService.readById(u_id);
        if (user == null)
            throw new EntityNotFoundException();
        List<ToDo> todos = user.getMyTodos();
        todos.addAll(user.getOtherTodos());
        return new ArrayList<>(todos);
    }

    @PostMapping("/{u_id}/todos")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or authentication.principal.id == #u_id")
    ToDo createTodoByUser(@PathVariable Long u_id, @Valid @RequestBody ToDo newToDo) {
        User user = userService.readById(u_id);
        if (user == null)
            throw new EntityNotFoundException();
        ToDo toDo = new ToDo();
        toDo.setTitle(newToDo.getTitle());
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(user);
        return toDoService.create(toDo);
    }

    @GetMapping("/{u_id}/todos/{t_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    ToDo getTodoByUser(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id) {
        return toDoService.readById(t_id);
    }

    @PutMapping("/{u_id}/todos/{t_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    ToDo updateTodoByUser(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id, @Valid @RequestBody ToDo newToDo) {
        ToDo toDo = toDoService.readById(t_id);
        if (toDo == null || userService.readById(u_id) == null)
            throw new EntityNotFoundException();
        toDo.setTitle(newToDo.getTitle());
        return toDoService.update(toDo);
    }

    @DeleteMapping("/{u_id}/todos/{t_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    ToDo deleteTodoByUser(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id) {
        ToDo toDo = toDoService.readById(t_id);
        if (toDo == null || userService.readById(u_id) == null)
            throw new EntityNotFoundException();
        toDoService.delete(t_id);
        return toDo;
    }

    @GetMapping("/{u_id}/todos/{t_id}/collaborators")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    List<UserResponse> getTodoCollaborators(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id) {
        ToDo toDo = toDoService.readById(t_id);
        if (toDo == null || userService.readById(u_id) == null)
            throw new EntityNotFoundException();
        return toDo.getCollaborators().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/{u_id}/todos/{t_id}/collaborators")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    UserResponse postTodoCollaborators(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id, @Valid @RequestBody User user) {
        User collaborator = userService.getAll().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        ToDo toDo = toDoService.readById(t_id);
        if (toDo == null)
            throw new EntityNotFoundException();
        toDo.getCollaborators().add(collaborator);
        return new UserResponse(collaborator);
    }

    @DeleteMapping("/{u_id}/todos/{t_id}/collaborators")
    UserResponse deleteTodoCollaborators(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id, @Valid User user) {
        User collaborator = userService.getAll().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        ToDo toDo = toDoService.readById(t_id);
        if (toDo == null)
            throw new EntityNotFoundException();
        toDo.getCollaborators().remove(collaborator);
        return new UserResponse(collaborator);
    }

    @GetMapping("/{u_id}/todos/{t_id}/tasks")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    List<Task> getTodoTasks(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id) {
        ToDo toDo = toDoService.readById(t_id);
        if (toDo == null || userService.readById(u_id) == null)
            throw new EntityNotFoundException();
        return new ArrayList<>(toDo.getTasks());
    }

    @PostMapping("/{u_id}/todos/{t_id}/tasks")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    Task createTodoTask(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id, @Valid @RequestBody Task newTask) {
        Task task = new Task();
        task.setName(newTask.getName());
        task.setPriority(Priority.valueOf(newTask.getPriority().name().toUpperCase()));
        task.setTodo(toDoService.readById(t_id));
        task.setState(stateService.getByName("NEW"));
        return (taskService.create(task));
    }

    @GetMapping("/{u_id}/todos/{t_id}/tasks/{task_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    Task getTodoTask(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id, @PathVariable("task_id") Long task_id) {
        Task task = taskService.readById(task_id);
        if (toDoService.readById(t_id) == null || userService.readById(u_id) == null || task == null)
            throw new EntityNotFoundException();
        return (task);
    }

    @PutMapping("/{u_id}/todos/{t_id}/tasks/{task_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    Task updateTodoTask(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id,
                        @PathVariable("task_id") Long task_id, @Valid @RequestBody Task newTask) {
        Task task = taskService.readById(task_id);
        if (toDoService.readById(t_id) == null || userService.readById(u_id) == null || task == null)
            throw new EntityNotFoundException();
        task.setName(newTask.getName());
        task.setPriority(Priority.valueOf(newTask.getPriority().name().toUpperCase()));
        task.setState(stateService.getByName(newTask.getState().getName().toUpperCase()));
        taskService.update(task);
        return (task);
    }

    @DeleteMapping("/{u_id}/todos/{t_id}/tasks/{task_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (authentication.principal.id == #u_id and @userController.isOwner(#t_id))")
    Task deleteTodoTask(@PathVariable("u_id") Long u_id, @PathVariable("t_id") Long t_id, @PathVariable("task_id") Long task_id) {
        Task oldTask = taskService.readById(task_id);
        if (toDoService.readById(t_id) == null || userService.readById(u_id) == null || oldTask == null)
            throw new EntityNotFoundException();
        taskService.delete(task_id);
        return oldTask;
    }

    public boolean isOwner(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean result = false;
        if (authentication != null && authentication.isAuthenticated()) {
            User userDetails = (User) authentication.getPrincipal();
            long userId = userDetails.getId();
            ToDo todo = toDoService.readById(id);
            result = todo != null && todo.getOwner().getId() == userId;
        }
        return result;
    }
}
