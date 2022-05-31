package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.AlreadyExistsException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.converter.UserConverter;
import com.lemberskay.reactpizza.model.dto.UserDto;
import com.lemberskay.reactpizza.model.entity.User;
import com.lemberskay.reactpizza.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    public UserController(UserService userService, UserConverter userConverter){
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @GetMapping()
    public List<UserDto> getAllUsers() throws ServiceException{
       List<User> users = userService.getAllUsers();
       List<UserDto> userResponseDtoList = new ArrayList<>();
       for(User user : users){
           UserDto userResponseDto = userConverter.convertToDto(user);
           userResponseDtoList.add(userResponseDto);
       }
       return userResponseDtoList;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) throws ServiceException{
        User user = userService.getUserById(id);
        return userConverter.convertToDto(user);
    }

    @GetMapping(params = "name")
    public UserDto getUserByName(@RequestParam("name") String name) throws ServiceException{
        User user =  userService.getUserByLogin(name);
        return userConverter.convertToDto(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable("id") Long id) throws ServiceException{
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserDto userRequestDto) throws ServiceException{
        User user = userConverter.convertToEntity(userRequestDto);
        User updatedUser = userService.updateUser(id, user);
        return userConverter.convertToDto(updatedUser);
    }

    @PostMapping()
    public ResponseEntity<Boolean> createUser(@RequestBody User user) throws AlreadyExistsException, ServiceException {
        boolean isRegistered = true;
        userService.createUser(user);
        return new ResponseEntity<>(isRegistered, HttpStatus.CREATED);
    }
}
