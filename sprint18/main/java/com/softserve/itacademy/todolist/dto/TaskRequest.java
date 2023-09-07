package com.softserve.itacademy.todolist.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TaskRequest {
    @NotBlank(message = "The 'name' cannot be empty")
    private String name;

    @NotNull
    private String priority;

    @NotNull
    private Long todoId;

    @NotNull
    private Long stateId;

    public TaskRequest() {
    }

    public TaskRequest(String name, String priority, Long todoId, Long stateId) {
        this.name = name;
        this.priority = priority;
        this.todoId = todoId;
        this.stateId = stateId;
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

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    @Override
    public String toString() {
        return "TaskRequestDto { " +
                "name = '" + name + '\'' +
                ", priority = '" + priority + '\'' +
                ", todoId = " + todoId +
                ", stateId = " + stateId +
                " }";
    }
}
