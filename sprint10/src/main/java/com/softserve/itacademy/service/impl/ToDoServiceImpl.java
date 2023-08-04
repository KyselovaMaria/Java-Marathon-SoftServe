package com.softserve.itacademy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;

@Service
public class ToDoServiceImpl implements ToDoService {

    private UserService userService;

    @Autowired
    public ToDoServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public ToDo addTodo(ToDo todo, User user) {
        if (todo == null) throw new IllegalArgumentException("ToDo can't be null");
        if (user == null) throw new IllegalArgumentException("User can't be null");
        if (user.getMyTodos() == null) {
            user.setMyTodos(new ArrayList<>());
        }
        user.getMyTodos().add(todo);
        todo.setOwner(user);
        return todo;
    }

    public ToDo updateTodo(ToDo todo) {
        if (todo == null) throw new IllegalArgumentException("ToDo can't be null");
        ToDo searched = userService.getAll().stream()
                .flatMap(x -> x.getMyTodos().stream())
                .filter(x -> x.getTitle().equals(todo.getTitle()) && x.getOwner().equals(todo.getOwner()))
                .findFirst().orElseThrow(()-> new RuntimeException("ToDo with given title and user wasn't found"));
        searched.setTasks(todo.getTasks());
        searched.setCreatedAt(todo.getCreatedAt());
        return searched;
    }

    public void deleteTodo(ToDo todo) {
        if (todo == null) return;
        userService.getAll().stream().map(User::getMyTodos)
                .forEach(x->x.remove(todo));
    }

    public List<ToDo> getAll() {
        return userService.getAll().stream()
                .flatMap(x->x.getMyTodos().stream())
                .collect(Collectors.toList());
    }

    public List<ToDo> getByUser(User user) {
        if (user == null) throw new IllegalArgumentException("User can't be null");
        return user.getMyTodos();
    }

    public ToDo getByUserTitle(User user, String title) {
        if (user == null) throw new IllegalArgumentException("User can't be null");
        for (ToDo todo : user.getMyTodos()) {
            if (todo.getTitle().equals(title)) return todo;
        }
        throw new RuntimeException("ToDo with given name wasn't found");
    }
}
