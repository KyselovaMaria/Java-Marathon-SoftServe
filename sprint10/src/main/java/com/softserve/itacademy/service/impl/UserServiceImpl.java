package com.softserve.itacademy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private List<User> users;

    public UserServiceImpl() {
        users = new ArrayList<>();
    }

    @Override
    public User addUser(User user) {
        if(user == null){
            throw new IllegalArgumentException("User is null!");
        }
        for (User user1 :users){
            if(user1.getEmail().equals(user.getEmail())){
                throw  new IllegalArgumentException("User " + user.toString() + " already exist!");
            }
        }
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if(user == null){
            throw new IllegalArgumentException("User is null!");
        }
        for (User u : users){
            if (u.getEmail().equals(user.getEmail())){
                u.setFirstName(user.getFirstName());
                u.setLastName(user.getLastName());
                u.setPassword(user.getPassword());
                u.getMyTodos().addAll(user.getMyTodos());
                return user;
            }
        }
        throw new IllegalArgumentException("User " + user.toString() + "can not found!");
    }

    @Override
    public void deleteUser(User user) {
        if(!users.remove(user)){
            throw new IllegalArgumentException("User" + user.toString() + " can not find!");
        }
    }

    @Override
    public List<User> getAll() {
        return users;
    }

}
