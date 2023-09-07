package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.Task.TaskResponse;
import com.softserve.itacademy.todolist.dto.todo.ToDoDtoFetchedCollaborators;
import com.softserve.itacademy.todolist.dto.todo.ToDoDto;
import com.softserve.itacademy.todolist.dto.todo.ToDoResponse;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final ToDoService toDoService;
    private final TaskService taskService;

    @GetMapping("/{id}")
    @PreAuthorize("@TodoAccess.canCreateUpdateOrGetAllTodos(#id)")
    public ResponseEntity<ToDoResponse> get(@PathVariable("id") Long id) {
        ToDoResponse toDoDto = toDoService.readDtoById(id);
        return ResponseEntity.ok(toDoDto);
    }

    @PostMapping
    public ResponseEntity<ToDoResponse> create(@Valid @RequestBody ToDoDto toDoDto) {
        ToDoResponse toDoResponse = toDoService.create(toDoDto);
        return new ResponseEntity<>(toDoResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@TodoAccess.canCreateUpdateOrGetAllTodos(#id)")
    public ResponseEntity<ToDoResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ToDoDto toDoDto) {
        ToDoResponse toDoResponse = toDoService.update(id, toDoDto);
        return ResponseEntity.ok(toDoResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@TodoAccess.canDeleteTodo(#id)")
    public ResponseEntity<ToDoResponse> delete(@PathVariable("id") Long id) {
        ToDoResponse toDoResponse = toDoService.delete(id);
        return ResponseEntity.ok(toDoResponse);
    }

    @PostMapping("/{id}/add/{collaborator_id}")
    @PostAuthorize("@TodoAccess.canCreateUpdateOrGetAllTodos(#id)")
    public ResponseEntity<ToDoDtoFetchedCollaborators> addCollaborator(@PathVariable("id") Long id,
                                                                       @PathVariable("collaborator_id") Long collaboratorId) {
        ToDoDtoFetchedCollaborators todoDto = toDoService.addCollaborator(id, collaboratorId);
        return ResponseEntity.ok(todoDto);
    }

    @DeleteMapping("/{id}/delete/{collaborator_id}")
    @PostAuthorize("@TodoAccess.canCreateUpdateOrGetAllTodos(#id)")
    public ResponseEntity<ToDoDtoFetchedCollaborators> deleteCollaborator(@PathVariable("id") Long id,
                                                                       @PathVariable("collaborator_id") Long collaboratorId) {
        ToDoDtoFetchedCollaborators todoDto = toDoService.deleteCollaborator(id, collaboratorId);
        return ResponseEntity.ok(todoDto);
    }

    @GetMapping("/{todo_id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@TodoAccess.canCreateUpdateOrGetAllTodos(#todoId)")
    public List<TaskResponse> getAllTodoTasks(@PathVariable("todo_id") long todoId){
        List<Task> tasks = taskService.getByTodoId(todoId);
        log.info("Get all tasks from todo id {}", todoId);
        return tasks.stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
    }
}
