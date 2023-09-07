package com.softserve.itacademy.todolist.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.Task;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponse {
    long id;
    String name;
    String priority;
    String state;

    public TaskResponse(Task task){
        this.id = task.getId();
        this.name = task.getName();
        this.priority = task.getPriority().name();
        this.state = task.getState().getName();
    }

    @Override
    public String toString() {
        return "TaskResponse { " +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priority='" + priority + '\'' +
                ", state='" + state + '\'' +
                " }";
    }
}
