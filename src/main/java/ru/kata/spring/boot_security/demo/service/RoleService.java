package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public void add(Role role) {
        roleRepository.save(role);
    }

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }


    public Optional<Role> findRoleById(int id) {
        return roleRepository.findById(id);
    }
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role getRoleById(int id) {
        return roleRepository.getRoleById(id);
    }

}
