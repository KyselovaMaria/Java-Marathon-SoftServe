package com.softserve.itacademy.todolist.dto.Task;

import com.softserve.itacademy.todolist.dto.Task.TaskDto;
import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.State;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;
import org.springframework.stereotype.Component;

@Component
public class TaskTransformer {
    public TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getName(),
                task.getPriority().toString(),
                task.getTodo().getId(),
                task.getState().getId()
        );
    }

    public static Task convertToEntity(TaskDto taskDto, ToDo todo, State state) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setPriority(Priority.valueOf(taskDto.getPriority().toUpperCase()));
        task.setTodo(todo);
        task.setState(state);
        return task;
    }
}
