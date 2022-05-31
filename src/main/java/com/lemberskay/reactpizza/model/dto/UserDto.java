package com.lemberskay.reactpizza.model.dto;

import com.lemberskay.reactpizza.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserDto {
    private long id;
    private String login;
    private String password;
    private User.Role role;
}
