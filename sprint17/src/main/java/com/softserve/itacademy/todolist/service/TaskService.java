package com.softserve.itacademy.todolist.service;

import com.softserve.itacademy.todolist.dto.Task.TaskDto;
import com.softserve.itacademy.todolist.model.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskService {
    Task create(TaskDto task);
    Task readById(long id);
    Task update(long id, TaskDto taskDto);
    void delete(long id);
    List<Task> getAll();

    List<Task> getByTodoId(long todoId);
}
