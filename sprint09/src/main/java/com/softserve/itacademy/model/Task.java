package com.softserve.itacademy.model;

public class Task {
    private int id;
    private String title;
    private Priority priority;

    private static int counter = 1;

    public Task() {
        id = counter++;
    }

    public Task(int id) {
        this.id = id;
    }

    public Task(String title, Priority priority) {
        this.title = title;
        this.priority = priority;
        id = counter++;
    }

    public Task(int taskId, String title, Priority priority) {
        this.id = taskId;
        this.title = title;
        this.priority = priority;
        // Adjust the counter to prevent possible id conflicts
        if (taskId >= counter) {
            counter = taskId + 1;
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                '}';
    }
}
