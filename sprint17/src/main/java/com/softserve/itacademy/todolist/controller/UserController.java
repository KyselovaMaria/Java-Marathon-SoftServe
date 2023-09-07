package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.UserRequest;
import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.dto.todo.ToDoResponse;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.RoleService;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ToDoService toDoService;

    @GetMapping
    List<UserResponse> getAll() {
        return userService.getAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    UserResponse getById(@PathVariable long id) {
        return new UserResponse(userService.readById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> create(@RequestBody UserRequest userRequest) {
        User user = userRequest.getEntity();
        user.setRole(roleService.readById(2));
        long userId = userService.create(user).getId();
        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/api/users/{id}").build()
                .expand(userId).toUri();
        return ResponseEntity
                .created(location)
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@UserAccess.canReadOrUpdate(#id)")
    ResponseEntity<Void> update(@PathVariable long id, @RequestBody UserRequest userRequest) throws URISyntaxException {
        User user = userRequest.getEntity(id);
        user.setRole(roleService.readById(2));
        long userId = userService.update(user).getId();
        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/api/users/{id}").build()
                .expand(userId).toUri();
        return ResponseEntity.ok()
                .header("Location", String.valueOf(location))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@UserAccess.canReadOrUpdate(#id)")
    ResponseEntity<User> delete(@PathVariable long id) {
        User user = userService.delete(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/todos")
    @PreAuthorize("@UserAccess.canReadOrUpdate(#id)")
    public ResponseEntity<List<ToDoResponse>> getAllUserTodos(@PathVariable("id") Long id) {
        List<ToDoResponse> todos = toDoService.getByUserId(id);
        return ResponseEntity.ok(todos);
    }
}
