package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/todos")
public class ToDoController {

    private final ToDoService toDoService;

    private final UserService userService;

    private final TaskService taskService;

    @Autowired
    public ToDoController(ToDoService toDoService, UserService userService, TaskService taskService) {
        this.toDoService = toDoService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/create/users/{owner_id}")
    public String create(
            @PathVariable(name = "owner_id") Long id,
            Model model
    ) {
        model.addAttribute("todo", new ToDo());
        model.addAttribute("user", userService.readById(id));
        return "create-todo";
    }

    @PostMapping("/create/users/{owner_id}")
    public String create(
            @PathVariable(name = "owner_id") Long id,
            @Valid @ModelAttribute("todo") ToDo toDo
    ) {
        User user = userService.readById(id);
        toDo.setOwner(user);
        toDo.setCreatedAt(LocalDateTime.now());

        toDoService.create(toDo);
        return "redirect:/todos/all/users/" + id;
    }

    @GetMapping("/{id}/tasks")
    public String read(
            @PathVariable(name = "id") Long id,
            Model model
    ) {
        ToDo toDo = toDoService.readById(id);
        List<User> freeUsers = userService.getAll();
        freeUsers.removeAll(toDo.getCollaborators());

        model.addAttribute("todo", toDo);
        model.addAttribute("tasks", taskService.getByTodoId(id));
        model.addAttribute("freeUsers", freeUsers);
        return "todo-tasks";
    }

    @GetMapping("/{todo_id}/update/users/{owner_id}")
    public String update(
            @PathVariable(name = "todo_id") Long todoId,
            @PathVariable(name = "owner_id") Long ownerId,
            Model model
    ) {
        model.addAttribute("todo", toDoService.readById(todoId));
        model.addAttribute("user", userService.readById(ownerId));
        model.addAttribute("collaborators", toDoService.readById(todoId).getCollaborators());
        return "update-todo";
    }

    @PostMapping("/{todo_id}/update/users/{owner_id}")
    public String update(
            @PathVariable(name = "todo_id") Long todoId,
            @PathVariable(name = "owner_id") Long id,
            @Valid @ModelAttribute("todo") ToDo toDo
    ) {
        List<User> collaborators = toDoService.readById(todoId).getCollaborators();
        User user = userService.readById(id);
        toDo.setOwner(user);
        toDo.setCollaborators(collaborators);

        toDoService.update(toDo);
        return "redirect:/todos/all/users/" + id;
    }

    @GetMapping("/{todo_id}/delete/users/{owner_id}")
    public String delete(
            @PathVariable(name = "todo_id") Long todoId,
            @PathVariable(name = "owner_id") Long ownerId
    ) {
        toDoService.delete(todoId);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/all/users/{id}")
    public String getAll(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.readById(id));
        model.addAttribute("todos", toDoService.getByUserId(id));
        return "todos-user";
    }

    @GetMapping("/{id}/add")
    public String addCollaborator(
            @PathVariable(name = "id") Long todoId,
            @RequestParam("selectedUser") Long selectedUserId
    ) {
        ToDo toDo = toDoService.readById(todoId);
        toDo.getCollaborators().add(userService.readById(selectedUserId));
        toDoService.update(toDo);

        return "redirect:/todos/{id}/tasks";
    }

    @GetMapping("/{id}/remove")
    public String removeCollaborator(
            @PathVariable(name = "id") Long id,
            @RequestParam Long collaboratorId
    ) {
        ToDo toDo = toDoService.readById(id);
        toDo.getCollaborators().remove(userService.readById(collaboratorId));
        toDoService.update(toDo);

        return "redirect:/todos/{id}/tasks";
    }
}
