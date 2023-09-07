package com.softserve.itacademy.todolist.service;

import com.softserve.itacademy.todolist.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    User create(User user);
    User readById(long id);
    User update(User user);
    User delete(long id);
    List<User> getAll();
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
