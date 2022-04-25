package ru.kata.spring.boot_security.demo.loader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
public class EntityLoader implements InitializingBean {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {
        Role adminRole  = new Role("ADMIN");
        Role userRole = new Role("USER");

        roleService.add(adminRole);
        roleService.add(userRole);

        User[] users=new User[] {
                new User("Ivan", "Ivanov", (byte)14,"ivan@mail.com"

                        ,passwordEncoder.encode("pro100pass"), Set.of(adminRole)),

                new User("Petr", "Petrov", (byte) 63,"petr@mail.com"

                        ,passwordEncoder.encode("pro100pass"),Set.of(userRole))
        };
        userService.saveUser(users[0]);
        userService.saveUser(users[1]);
    }
}
