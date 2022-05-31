package com.lemberskay.reactpizza.model.converter;

import com.lemberskay.reactpizza.model.dto.UserDto;
import com.lemberskay.reactpizza.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements BaseConverter<UserDto, User> {
    @Override
    public UserDto convertToDto(User user){
        return UserDto.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .id(user.getId())
                .role(user.getRole())
                .build();
    }

    @Override
    public User convertToEntity(UserDto userDto) {
        return User.builder()
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .build();
    }
}
