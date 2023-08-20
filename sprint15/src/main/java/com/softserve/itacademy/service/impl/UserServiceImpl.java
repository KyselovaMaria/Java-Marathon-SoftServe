package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.EntityNotFoundException;
import com.softserve.itacademy.exceptions.NullEntityReferenceException;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
            return userRepository.save(user);
    }

    @Override
    public User readById(long id) {
        Optional<User> optional = userRepository.findById(id);
            return optional.get();
    }

    @Override
    public User update(User user) {
        try {
            User oldUser = readById(user.getId());
        } catch (NoSuchElementException e) {
            throw new NullEntityReferenceException("Entity was given id=" + user.getId() + " references to null!");
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        User user;
        try {
            user = readById(id);
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundException("Entity was given id=" + id + " was not found!");
        }
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }

}
