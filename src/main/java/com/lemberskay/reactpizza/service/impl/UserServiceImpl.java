package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.AlreadyExistsException;
import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.User;
import com.lemberskay.reactpizza.repository.impl.JdbcUserRepository;
import com.lemberskay.reactpizza.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private JdbcUserRepository jdbcUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(JdbcUserRepository jdbcUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.jdbcUserRepository = jdbcUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        try{
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(username);
            if(!optionalUser.isPresent()){
                log.error(String.format("Failed to get user with username: %s from database", username));
                throw new UsernameNotFoundException(String.format("User %s not found", username));
            }
            return optionalUser.get();
        } catch(DaoException e){
            log.error(String.format("Failed to get user with username: %s from database", username), e);
        }
        return new User();
    }


    @Override
    public List<User> getAllUsers() throws ServiceException {
        try{
            return jdbcUserRepository.findAll();
        } catch(DaoException e){
            log.error("Failed to get users from database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public User getUserByLogin(String userName) throws ServiceException {
        try{
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(userName);
            if(optionalUser.isEmpty()){
                throw new ResourceNotFoundException("Users","username",userName);
            }
            return optionalUser.get();
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    @Override
    public User getUserById(long id) throws ServiceException {
        try{
            Optional<User> optionalUser = jdbcUserRepository.findById(id);
            if(!optionalUser.isPresent()){
                throw new ResourceNotFoundException("Users","id",id);
            }
            return optionalUser.get();
        } catch(DaoException e){
            log.error(String.format("Failed to get user with id: %s from database", id), e);
            throw new ServiceException(e);
        }
    }



    @Override
    @Transactional
    public User createUser(User user) throws ServiceException {
        try{
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(user.getUsername());
           if(optionalUser.isPresent()) {
               throw new AlreadyExistsException("User", "username", user.getUsername());
           }
           user.setRole(User.Role.ROLE_USER);
           user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
           log.info("User is in process of creating...");
           return jdbcUserRepository.insert(user);
        } catch(DaoException e){
            log.error("Failed to insert user into database");
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public User updateUser(long id, User user) throws ServiceException {
        try{
            Optional<User> optionalUser = jdbcUserRepository.findById(id);
            if(optionalUser.isEmpty()){
                throw new ResourceNotFoundException("Users","id",id);
            }
            if(user.getLogin()== null){
                user.setLogin(optionalUser.get().getLogin());
            }
            if(user.getPassword()== null){
                user.setPassword(optionalUser.get().getPassword());
            }
            if(user.getRole() == null){
                user.setRole(optionalUser.get().getRole());
            }
            return jdbcUserRepository.update(id, user);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(long userId) throws ServiceException {
        try{
            if(jdbcUserRepository.findById(userId).isPresent()){
                log.info("User is in process of deleting...");
                return jdbcUserRepository.remove(userId);
            }
            else {
                log.error(String.format("Failed to get user with id: %s from database", userId));
                throw new ResourceNotFoundException("Users", "id",userId);
            }
        } catch(DaoException e){
            log.error(String.format("Failed to remove user with id: %s from database", userId),e);
            throw new ServiceException(e);
        }
    }
}
