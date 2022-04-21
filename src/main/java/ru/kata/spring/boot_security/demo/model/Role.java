package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@Data
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    public Role(){}

    public Role(int id){
        this.id = id;
    }
    public Role(int id,String name){
        this.id = id;
        this.name = name;
    }
    public Role(String  role){
        this.name = role;
    }
    @Override
    public String getAuthority() {
        return name;
    }
}