package com.softserve.itacademy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitPlatform.class)
public class UserServiceTest {
    private static UserService userService;

    @BeforeAll
    public static void setupBeforeClass() {
        AnnotationConfigApplicationContext annotationConfigContext = new AnnotationConfigApplicationContext(Config.class);
        userService = annotationConfigContext.getBean(UserService.class);
        annotationConfigContext.close();
    }

    @AfterEach
    public void clearBeforeNext() {
        userService.getAll().forEach(user -> user.getMyTodos().clear());
        userService.getAll().clear();
    }

    @Test
    public void checkAddUser() {
        int sizeUsersListBegin = userService.getAll().size();
        User user = new User("John", "Doe", "johndoe@gmail.com", "p@ssw0rd123");
        User actual = userService.addUser(user);
        int sizeUsersListEnd = userService.getAll().size();
        Assertions.assertEquals(user, actual, "user added");
        Assertions.assertEquals(sizeUsersListEnd, sizeUsersListBegin+1);
    }

    @Test
    public void checkAddUserWithExistEmail(){
        User user1 = new User("Alice", "Jobs", "jobs@gmail.com","3q357");
        userService.addUser(user1);
        User user2 = new User("Steve", "Jobs", "jobs@gmail.com","89579");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.addUser(user2));
    }

    @Test
    public void checkAddNotExistUser(){
        User user = null;
        Assertions.assertThrows(IllegalArgumentException.class,()-> userService.addUser(user));
    }

    @Test
    public void checkUpdateUserByNotExistEmail(){
        User user = new User("Anna","Jayson", "jayson@gmail.com","gthrth7u6");
        Assertions.assertThrows(IllegalArgumentException.class,() -> userService.updateUser(user));
    }

    @Test
    public void checkUpdateUserByEmail(){
        User user = new User("Amanda","Jackson", "ajack@gmail.com","gthrth7u6");
        userService.addUser(user);
        user = new User("Amanda","Jacksson", "ajack@gmail.com","gthrth767u6");
        User actual = userService.updateUser(user);
        User expected =  new User("Amanda","Jacksson", "ajack@gmail.com","gthrth767u6");
        Assertions.assertEquals(expected,actual);
    }


    @Test
    public void checkUpdateNotExistUser(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> userService.updateUser(null));
    }

    @Test
    public void checkDeleteUser() {
        User user = new User("Emily", "Brown", "emily.brown@hotmail.com", "1234pass");
        userService.addUser(user);

        User user1 = new User("Alice", "Smith", "alice.smith@gmail.com", "qwerty567");
        userService.addUser(user1);

        User user2 = new User("Bob", "Johnson", "bob.johnson@yahoo.com", "securePWD");
        userService.addUser(user2);

        // Check that the user is added successfully
        Assertions.assertTrue(userService.getAll().contains(user));
        Assertions.assertTrue(userService.getAll().contains(user1));
        Assertions.assertTrue(userService.getAll().contains(user2));

        // Delete the user
        userService.deleteUser(user);

        // Check that the deleted user is no longer in the list
        Assertions.assertFalse(userService.getAll().contains(user));
        Assertions.assertTrue(userService.getAll().contains(user1));
        Assertions.assertTrue(userService.getAll().contains(user2));

        // Check the size of the list after deletion
        Assertions.assertEquals(2, userService.getAll().size());
    }


    @Test
    public void checkDeleteNotExistUser(){
        User user = new User("Michael", "Williams", "michael.williams@gmail.com", "m1ch@el");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(user));
    }

    @Test
    public void checkGetAll(){
        User user1 = new User("Sophia", "Miller", "sophia.miller@yahoo.com", "securePWD321");
        userService.addUser(user1);
        User user2 = new User("David", "Lee", "david.lee@gmail.com", "qazwsx123");
        userService.addUser(user2);

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);

        List<User> actual = userService.getAll();
        Assertions.assertEquals(expected, actual);
    }
}