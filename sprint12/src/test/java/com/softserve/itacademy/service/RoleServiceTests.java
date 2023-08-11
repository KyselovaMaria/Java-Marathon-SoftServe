package com.softserve.itacademy.service;

import com.softserve.itacademy.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class RoleServiceTests {


    @Autowired
    private RoleService roleService;




    @Test
    public void testCreateRole() {
        Role role = new Role();
        role.setName("TestRole");

        Role createdRole = roleService.create(role);
        assertEquals("TestRole", createdRole.getName());
    }

    @Test
    public void testReadRoleById() {
        Role retrievedRole = roleService.readById(1L);
        assertNotNull(retrievedRole);
        assertEquals("ADMIN", retrievedRole.getName());
    }

    @Test
    public void testReadRoleByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> roleService.readById(1000L));
    }

    @Test
    public void testUpdateRole() {
        Role role = new Role();
        role.setName("OldRoleName");
        Role updatedRole = roleService.create(role);
        updatedRole.setName("NewRoleName");
        Role result = roleService.update(updatedRole);
        assertEquals("NewRoleName", result.getName());
    }

    @Test
    public void testUpdateRoleNotFound() {
        Role role = new Role();
        role.setName("TestUpdate");

        assertThrows(EntityNotFoundException.class, () -> roleService.update(role));
    }

    @Test
    public void testDeleteRole() {
        Role role = new Role();
        role.setName("RoleToDelete");
        role = roleService.create(role);
        long roleID = role.getId();
        assertEquals("RoleToDelete", roleService.readById(roleID).getName());
        roleService.delete(roleID);
        assertThrows(EntityNotFoundException.class,()->roleService.readById(roleID));

    }

    @Test
    public void testDeleteRoleNotFound() {
        assertThrows(EntityNotFoundException.class, () -> roleService.delete(10000L));
    }

    @Test
    public void testGetAllRoles() {
        Role role = new Role();
        role.setName("Role10");
        role = roleService.create(role);

        Role role1 = new Role();
        role1.setName("Role11");
        role1 = roleService.create(role);


        List<Role> roles = roleService.getAll();

        assertTrue(roles.size() >= 2);
    }



}
