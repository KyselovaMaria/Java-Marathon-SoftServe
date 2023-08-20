package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") User user) {
        userService.create(user);
        return "redirect:/home";
    }

    //    @GetMapping("/{id}/read")
//    public String read(//add needed parameters) {
//        //ToDo
//        return " ";
//    }
//
    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.readById(id));
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("newPassword", "");
        return "update-user";
    }

    @PostMapping("/{id}/update")
    public String updatePost(
//            @PathVariable Long id,
            @ModelAttribute("user") User user,
            @ModelAttribute("role.id") Long roleId,
            @ModelAttribute("newPassword") String newPassword
    ) {
        Role userRole = roleService.readById(roleId);
        user.setRole(userRole);
        user.setPassword(newPassword);
        userService.update(user);
        return "redirect:/home";
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/home";
    }

//    @GetMapping("/all")
//    public String getAll(//add needed parameters) {
//        //ToDo
//        return " ";
//    }
}
