package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.User;
import com.lemberskay.reactpizza.service.UserService;
import com.lemberskay.reactpizza.service.impl.UserServiceImpl;
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
    public ResponseEntity<Boolean> processRegistration(@RequestBody User user) throws ServiceException {
        boolean isRegistered = true;
        User registeredUser = userService.createUser(user);
        if(registeredUser==null) isRegistered = false;
        return new ResponseEntity<>(isRegistered, HttpStatus.CREATED);
    }
}
