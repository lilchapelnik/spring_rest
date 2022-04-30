package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MyRestController {

    private static User USER = new User();

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping ("/about")
    public User getCurrentUser(Principal principal){
        return userService.getByUserName(principal.getName());
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findUserById(id);
    }
    @DeleteMapping("/users/{id}")
    public boolean deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
        return true;
    }

    @PutMapping("/users")
    public User updateUserPost(@RequestBody User user) {
        User userDb = null;
        if (user.getRoles().size() == 0 || user.getPassword().equals("")) {
            userDb = userService.findUserById(user.getId());
        }
        if(user.getRoles().size() != 0) {
            Set<Role> rolesSet = new HashSet<>();
            for (Role r : user.getRoles()) {
                rolesSet.add(roleService.findByName(r.getName()));
            }
            user.setRoles(rolesSet);
        } else {
            user.setRoles(userDb.getRoles());
        }
        if (user.getPassword().equals("")) {
            user.setPassword(userDb.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.saveUser(user);
        return user;
    }
    @PostMapping("/users")
    public User addUser(@RequestBody User user){
        Set<Role> rolesSet = new HashSet<>();
        for(Role r: user.getRoles()){
            rolesSet.add(roleService.findByName(r.getName()));
        }
        user.setRoles(rolesSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return user;
    }

}
