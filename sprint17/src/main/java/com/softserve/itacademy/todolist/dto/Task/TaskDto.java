package com.softserve.itacademy.todolist.dto.Task;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TaskDto {
    private Long id;

    @NotBlank(message = "The 'name' cannot be empty")
    private String name;

    @NotNull
    private String priority;

    @NotNull
    private Long todoId;

    @NotNull
    private Long stateId;

    public TaskDto() {
    }

    public TaskDto(long id, String name, String priority, long todoId, long stateId) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.todoId = todoId;
        this.stateId = stateId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getTodoId() {
        return todoId;
    }

    public void setTodoId(long todoId) {
        this.todoId = todoId;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return id == taskDto.id && todoId == taskDto.todoId && stateId == taskDto.stateId && Objects.equals(name, taskDto.name) && Objects.equals(priority, taskDto.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, priority, todoId, stateId);
    }

    @Override
    public String toString() {
        return "TaskDto { " +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", priority = '" + priority + '\'' +
                ", todoId = " + todoId +
                ", stateId = " + stateId +
                " }";
    }
}
