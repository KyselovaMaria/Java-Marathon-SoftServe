package com.softserve.itacademy.todolist.service.impl;

import com.softserve.itacademy.todolist.dto.Task.TaskDto;
import com.softserve.itacademy.todolist.dto.Task.TaskTransformer;
import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.State;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.repository.TaskRepository;
import com.softserve.itacademy.todolist.repository.ToDoRepository;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskTransformer taskTransformer;
    private final TaskRepository taskRepository;
    private final ToDoService toDoService;
    private final StateService stateService;

    @Override
    public Task create(TaskDto taskDto) {
        if (taskDto != null) {
            ToDo toDo = toDoService.readById(taskDto.getTodoId());
            State state = stateService.readById(taskDto.getStateId());
            Task task = taskTransformer.convertToEntity(taskDto, toDo, state);
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public Task readById(long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    @Override
    public Task update(long id, TaskDto taskDto) {
        if (taskDto != null) {
            Task task = readById(id);
            task.setPriority(Priority.valueOf(taskDto.getPriority()));
            task.setName(taskDto.getName());
            task.setState(stateService.readById(taskDto.getStateId()));
            return task;
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Task task = readById(id);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        return taskRepository.getByTodoId(todoId);
    }
}
