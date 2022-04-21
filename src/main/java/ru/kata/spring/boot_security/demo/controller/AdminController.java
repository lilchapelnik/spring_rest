package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static User USER = new User();

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String blogMain(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("roles", user.printSet(user.getRoles()));

        Iterable<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/user-delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }


    @GetMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") int id,Model model) {
        User user = userService.findById(id);
        user.setPassword("");
        model.addAttribute("user",user);
        return "admin/user-update";
    }

    @PutMapping("/user-update/{id}")
    public String updateUserPost(@ModelAttribute("user") User userForm,
                                 @PathVariable("id") int id,
                                 @RequestParam(name = "role", required = false) String[] roles) {
        Set<Role> rolesSet = new HashSet<>();
        if (roles != null) {
            for(String s: roles){
                rolesSet.add(roleService.findByName(s));
            }
            userForm.setRoles(rolesSet);
            if (userForm.getPassword() != null) {
                userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
            } else {
                userForm.setPassword(userService.getByUserName(userForm.getUserName()).getPassword());
            }
            userService.saveUser(userForm);
        }
        return "redirect:/admin";
    }

    @PostMapping("/user-create")
    public String addUser(@ModelAttribute("user")User user,
                          @RequestParam(name = "role", required = false) String[] roles){
        Set<Role> rolesSet = new HashSet<>();
        for(String s: roles){
            rolesSet.add(roleService.findByName(s));
        }
        user.setRoles(rolesSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/user-create")
    public String createUserHtml(Model model) {
        model.addAttribute("user", USER);
        return "admin/user-create";
    }

}


