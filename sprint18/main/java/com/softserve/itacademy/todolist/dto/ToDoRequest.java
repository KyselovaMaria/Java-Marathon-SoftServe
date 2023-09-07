package com.softserve.itacademy.todolist.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ToDoRequest {
    @NotBlank(message = "The 'title' cannot be empty")
    private String title;

    @NotNull(message = "The 'ownerId' cannot be null")
    private Long ownerId;

    public ToDoRequest() {
    }

    public ToDoRequest(String title, Long ownerId) {
        this.title = title;
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "ToDoRequest { " +
                "title = '" + title + '\'' +
                ", ownerId = " + ownerId +
                " }";
    }
}
