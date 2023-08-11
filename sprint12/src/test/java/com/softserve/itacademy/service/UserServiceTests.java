package com.softserve.itacademy.service;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserServiceTests(UserService userService) {
        this.userService = userService;
    }

    private Role role;




    @Test
    public void getAllUsersTest() {
        int expectedSize = 3;
        List<User> users = userService.getAll();
        assertTrue(expectedSize <= users.size(), String.format("At least %d users shuold be in users table", expectedSize));
    }

    @Test
    @Transactional
    public void testCreateUser() {

        Role role = new Role();
        role.setName("Role1");
        role = roleRepository.save(role);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("P@ssw0rd");
        user.setRole(role);

        User createdUser = userService.create(user);
        assertNotNull(createdUser.getId());
    }

    @Test
    @Transactional
    public void testReadUserById() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("janedoe@example.com");
        user.setPassword("P@ssw0rd");
        user.setRole(role);

        User createdUser = userService.create(user);

        User retrievedUser = userService.readById(createdUser.getId());
        assertNotNull(retrievedUser);
        assertEquals("Jane", retrievedUser.getFirstName());
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Smith");
        user.setEmail("alicesmith@example.com");
        user.setPassword("P@ssw0rd");

        Role role = new Role();
        role.setName("TestRole");
        role = roleRepository.save(role);

        user.setRole(role);
        User createdUser = userService.create(user);

        createdUser.setFirstName("Updatedalice");
        User updatedUser = userService.update(createdUser);

        assertNotNull(updatedUser);
        assertEquals("Updatedalice", updatedUser.getFirstName());
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Johnson");
        user.setEmail("bobjohnson@example.com");
        user.setPassword("P@ssw0rd");

        Role role = new Role();
        role.setName("TestDeleteRole");
        role = roleRepository.save(role);

        user.setRole(role);

        User createdUser = userService.create(user);

        userService.delete(createdUser.getId());
        assertThrows(EntityNotFoundException.class, () -> userService.readById(createdUser.getId()));
    }
}