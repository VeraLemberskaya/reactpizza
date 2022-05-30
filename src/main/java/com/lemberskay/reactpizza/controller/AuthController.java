package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.AlreadyExistsException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.User;
import com.lemberskay.reactpizza.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> processRegistration(@RequestBody User user) throws AlreadyExistsException, ServiceException {
        boolean isRegistered = true;
        User registeredUser = userService.createUser(user);
        return new ResponseEntity<>(isRegistered, HttpStatus.CREATED);
    }
}
