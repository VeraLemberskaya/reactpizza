package com.lemberskay.reactpizza.service;

import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{
    List<User> getAllUsers() throws ServiceException;

    User getUserByLogin(String userName) throws ServiceException;
    User getUserById(long id) throws  ServiceException;

    User createUser(User user) throws ServiceException;

    User updateUser(long id, User user) throws ServiceException;

    boolean deleteUser(long userId) throws ServiceException;
}
