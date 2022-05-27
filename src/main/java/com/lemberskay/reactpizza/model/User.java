package com.lemberskay.reactpizza.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class User extends AbstractEntity{
    @Email
    private String email;
    private String password;
    private String name;

    public User(){super();}
    public User(long id,String email, String password, String name){
        super(id);
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
