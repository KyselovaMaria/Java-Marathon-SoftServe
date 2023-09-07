package com.softserve.itacademy.todolist.security;

import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("UserAccess")
@AllArgsConstructor
public class UserAccessHandler {

    private final UserService userService;

    public boolean canReadOrUpdate(Long id) {
        User user = userService.readById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return user.getEmail().equals(authentication.getName());
    }
}
