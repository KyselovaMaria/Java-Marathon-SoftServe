package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {

        return roleRepository.save(role);
    }

    @Override
    public Role readById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with ID " + id + " not found"));
    }

    @Override
    public Role update(Role role) {
        if (roleRepository.existsById(role.getId())) {
            return roleRepository.save(role);
        }
        throw new EntityNotFoundException("Role with ID " + role.getId() + " not found");
    }

    @Override
    public void delete(long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Role with ID " + id + " not found");
        }
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
