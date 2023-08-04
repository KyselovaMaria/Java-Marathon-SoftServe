package com.softserve.itacademy.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;

@Service
public class TaskServiceImpl implements TaskService {

    private ToDoService toDoService;

    @Autowired
    public TaskServiceImpl(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @Override
    public Task addTask(Task task, ToDo todo) {
        if (task == null)
            throw new IllegalArgumentException("Task cannot be null!");
        if (todo == null)
            throw new IllegalArgumentException("ToDo cannot be null!");
        if (!toDoService.getAll().contains(todo))
            throw new IllegalArgumentException("There is no " + todo.getTitle() + " exist!");
        if (toDoService.getAll().stream()
                .flatMap(t -> t.getTasks().stream())
                .collect(Collectors.toList())
                .stream()
                .anyMatch(t -> t.getName().equals(task.getName())))
            throw new IllegalArgumentException("Task " + task.getName() + " already exist!");
        todo.getTasks().add(task);
        return todo.getTasks().get(todo.getTasks().indexOf(task));
    }

    @Override
    public Task updateTask(Task task) {
        if (task == null)
            throw new IllegalArgumentException("Task cannot be null!");
        Optional<Task> optionalTask = toDoService.getAll().stream()
                .flatMap(todo -> todo.getTasks().stream())
                .filter(t -> t.getName().equals(task.getName()))
                .findFirst();
        if (optionalTask.isPresent()) {
            optionalTask.get().setPriority(task.getPriority());
            return optionalTask.get();
        }
        throw new IllegalArgumentException("Task " + task.getName() + " is not found!");
    }

    @Override
    public void deleteTask(Task task) {
        if (task == null)
            throw new IllegalArgumentException("Task cannot be null!");
        Optional<ToDo> optionalToDo = toDoService.getAll().stream()
                .filter(todo -> todo.getTasks().contains(task)).findFirst();
        if (!optionalToDo.isPresent())
            throw new IllegalArgumentException("There is no ToDo list with " + task.getName() + " task in it!");
        if (!optionalToDo.get().getTasks().remove(task))
            throw new IllegalArgumentException("Removing a task " + task.getName() + " was unsuccessful!");
        optionalToDo.get().setTasks(optionalToDo.get().getTasks());
    }

    @Override
    public List<Task> getAll() {
        return toDoService.getAll().stream()
                .flatMap(todo -> todo.getTasks().stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getByToDo(ToDo todo) {
        if (todo == null)
            throw new IllegalArgumentException("ToDo cannot be null!");
        if (!toDoService.getAll().contains(todo))
            throw new IllegalArgumentException("There is no " + todo.getTitle() + " exist!");
        return todo.getTasks();
    }

    @Override
    public Task getByToDoName(ToDo todo, String name) {
        if (todo == null)
            throw new IllegalArgumentException("ToDo cannot be null!");
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name of Task cannot be null or empty!");
        if (!toDoService.getAll().contains(todo))
            throw new IllegalArgumentException("There is no " + todo.getTitle() + " exist!");
        Optional<Task> optionalTask = todo.getTasks().stream().filter(t -> t.getName().equals(name)).findFirst();
        if (optionalTask.isPresent())
            return optionalTask.get();
        throw new IllegalArgumentException("Task with name " + name + " is not found!");
    }

    @Override
    public Task getByUserName(User user, String name) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null!");
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name of Task cannot be null or empty!");
        Optional<Task> optionalTask = user.getMyTodos().stream()
                .flatMap(todo -> todo.getTasks().stream())
                .filter(task -> task.getName().equals(name))
                .findFirst();
        if (optionalTask.isPresent())
            return optionalTask.get();
        throw new IllegalArgumentException("Task with name " + name + " is not found!");
    }

}
