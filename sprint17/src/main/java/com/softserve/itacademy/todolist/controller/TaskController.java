package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.Task.TaskDto;
import com.softserve.itacademy.todolist.dto.Task.TaskResponse;
import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@TaskAccess.canReadTask(#id)")
    public TaskResponse getTask(@PathVariable("id") long id) {
        Task task = taskService.readById(id);
        return new TaskResponse(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@TaskAccess.canCreateOrGetAllTasks(#taskDto.todoId)")
    public TaskResponse createTask(@Valid @RequestBody TaskDto taskDto){
        try {
            Task task = taskService.create(taskDto);
            TaskResponse response = new TaskResponse(task);
            log.info("New task has been created - {}", response);
            return response;
        }catch (NullEntityReferenceException ex){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage()
            );
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("@TaskAccess.canUpdateTask(#id)")
    public TaskResponse updateTask(@PathVariable(name = "id") long id,
                                   @Valid @RequestBody TaskDto taskDto){
        try {
            Task task = taskService.update(id, taskDto);
            TaskResponse response = new TaskResponse(task);
            log.info("Task with id {} has been updated - {}", response.getId(), response);
            return response;
        } catch (NullEntityReferenceException exception){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("@TaskAccess.canDeleteTask(#taskId)")
    public ResponseEntity<String> deleteTask(@PathVariable("id") long taskId){
        try {
            taskService.delete(taskId);
        }catch (EntityNotFoundException ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage()
            );
        }
        log.info("Task with id {} has been removed", taskId);
        return ResponseEntity.accepted().body("Task with id " + taskId + " has been removed");
    }
}
