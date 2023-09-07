package com.softserve.itacademy.security;


import com.softserve.itacademy.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        com.softserve.itacademy.model.User customUser =
                userService.getAll().stream().
                        filter(u->u.getEmail().equals(login)).
                        findAny().orElseThrow(()->new UsernameNotFoundException(login + " not found"));
        List<GrantedAuthority> roles = List.of(
                new SimpleGrantedAuthority(customUser.getRole().toString()));

        return new User(customUser.getEmail(), customUser.getPassword(), roles);
    }
}

