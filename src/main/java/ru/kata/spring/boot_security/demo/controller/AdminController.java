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
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;


    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String blogMain(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("roles", user.printSet(user.getRoles()));

        Iterable<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/admin/home";
    }


    @GetMapping("/edit/{id}")
    public ModelAndView editPage(@PathVariable("id") int id, ModelAndView modelAndView) {
        User userById = userService.findUserById(id);
        modelAndView.setViewName("editUser");
        modelAndView.addObject("user", userById);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String updateUserPost(@ModelAttribute("user") User userForm,
                                 @RequestParam(name = "role", required = false) int id, String role) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userService.getRoleById(id));
        if ((role != null) && (role.equals("admin"))) {
            roleSet.add(userService.getRoleById(id));
        }
        userForm.setRoles(roleSet);
        userService.saveUser(userForm);
        return "redirect:/admin/home";
    }
    @GetMapping("/adduser")
    public String addUser() {
        return "addUser";
    }

    @PostMapping("/adduser")
    public String addUserPost(@ModelAttribute("user") User userForm, @RequestParam(name = "role", required = false) String role,int id) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userService.getRoleById(id));
        if ((role != null) && (role.equals("admin"))) {
            roleSet.add(userService.getRoleById(id));
        }
        userForm.setRoles(roleSet);
        userService.saveUser(new User(userForm.getName(),userForm.getLastname(),
                userForm.getUsername(), userForm.getPassword(), userForm.getRoles()));
        return "redirect:/admin/home";
    }
}


