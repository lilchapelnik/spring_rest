package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user")
    public ModelAndView user(ModelAndView modelAndView) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("myPage");
        return modelAndView;
    }

    @GetMapping("/registration")
    public String pageNewUser(){
        return "regPage";
    }

    @PostMapping("/registration")
    public String addNewUser(@ModelAttribute("user") User userForm,int id) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userService.getRoleById(id));
        userForm.setRoles(roleSet);
        userService.saveUser(new User(userForm.getName(),userForm.getLastname(),
                userForm.getUsername(), userForm.getPassword(), userForm.getRoles()));
        return "redirect:/login";
    }
}