package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testReadUserByEmail() {

        Role role = new Role();
        role.setName("Role2");
        role = roleRepository.save(role);

        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("janedoe@example.com");
        user.setPassword("P@ssw0rd");
        user.setRole(role);

        userRepository.save(user);

        User retrievedUser = userRepository.findByEmail("janedoe@example.com");
        assertNotNull(retrievedUser);
        assertEquals("Jane", retrievedUser.getFirstName());
    }

}
