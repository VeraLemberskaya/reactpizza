package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Address;
import com.lemberskay.reactpizza.model.User;
import com.lemberskay.reactpizza.repository.impl.JdbcAddressRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcUserRepository;
import com.lemberskay.reactpizza.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final JdbcAddressRepository jdbcAddressRepository;
    private final JdbcUserRepository jdbcUserRepository;

    public AddressServiceImpl(JdbcAddressRepository jdbcAddressRepository, JdbcUserRepository jdbcUserRepository){
        this.jdbcAddressRepository = jdbcAddressRepository;
        this.jdbcUserRepository = jdbcUserRepository;
    }

    @Override
    public List<Address> getAllAddresses() throws ServiceException {
        try{
            return jdbcAddressRepository.findAll();
        } catch (DaoException e){
            log.error("Failed to get addresses from database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Address> getAddressesByUser(String userName) throws ServiceException {
        try{
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(userName);
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                return jdbcAddressRepository.findByUserId(user.getId());
            } else {
                log.error(String.format("Failed to find user with username: %s", userName));
                throw new ResourceNotFoundException("Users","username",userName);
            }
        } catch (DaoException e){
            log.error(String.format("Failed to get address by username: %s from database", userName), e);
            throw new ServiceException(e);
        }
    }
}
