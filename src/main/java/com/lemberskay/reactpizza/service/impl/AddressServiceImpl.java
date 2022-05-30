package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.NotAuthorizedException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Address;
import com.lemberskay.reactpizza.model.Country;
import com.lemberskay.reactpizza.model.User;
import com.lemberskay.reactpizza.repository.impl.JdbcAddressRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcCountryRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcUserRepository;
import com.lemberskay.reactpizza.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final JdbcAddressRepository jdbcAddressRepository;
    private final JdbcUserRepository jdbcUserRepository;
    private final JdbcCountryRepository jdbcCountryRepository;

    public AddressServiceImpl(JdbcAddressRepository jdbcAddressRepository, JdbcUserRepository jdbcUserRepository, JdbcCountryRepository jdbcCountryRepository){
        this.jdbcAddressRepository = jdbcAddressRepository;
        this.jdbcUserRepository = jdbcUserRepository;
        this.jdbcCountryRepository = jdbcCountryRepository;
    }

    @Override
    @Transactional
    public List<Address> getAllAddresses() throws ServiceException {
        try{
            return jdbcAddressRepository.findAll();
        } catch (DaoException e){
            log.error("Failed to get addresses from database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public Address createAddress(Address address) throws ServiceException {
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(userName);
            Optional<Country> optionalCountry = jdbcCountryRepository.findById(address.getCountryId());
            if(optionalUser.isPresent()){
                if(optionalCountry.isPresent()){
                    address.setUserId(optionalUser.get().getId());
                    return jdbcAddressRepository.insert(address);
                } else{
                    log.error(String.format("Failed to find country with id: %s", address.getCountryId()));
                    throw new ResourceNotFoundException("Country","id",address.getCountryId());
                }
            } else {
                log.error(String.format("Failed to find user with id: %s", address.getUserId()));
                throw new ResourceNotFoundException("Users","id",address.getUserId());
            }
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Address updateAddress(long id, Address address) throws ServiceException {
        try{
            Optional<Address> optionalAddress = jdbcAddressRepository.findById(id);
            if(optionalAddress.isEmpty()){
                log.error(String.format("Failed to find address with id: %s", id));
                throw new ResourceNotFoundException("Addresses","id",id);
            }
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(userName);
            Optional<Country> optionalCountry = jdbcCountryRepository.findById(address.getCountryId());
            if(optionalUser.isPresent()){
                if(optionalCountry.isPresent()){
                    address.setUserId(optionalUser.get().getId());
                    return jdbcAddressRepository.update(id, address);
                } else{
                    log.error(String.format("Failed to find country with id: %s", address.getCountryId()));
                    throw new ResourceNotFoundException("Country","id",address.getCountryId());
                }
            } else {
                log.error(String.format("Failed to find user with id: %s", address.getUserId()));
                throw new ResourceNotFoundException("Users","id",address.getUserId());
            }
        }catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteAddress(long id) throws ServiceException,NotAuthorizedException {
        try{
            Optional<Address> optionalAddress = jdbcAddressRepository.findById(id);
           if(optionalAddress.isEmpty()){
               throw new ResourceNotFoundException("Addresses","id",id);
           }
           String userName = SecurityContextHolder.getContext().getAuthentication().getName();
           Optional<User> optionalUser = jdbcUserRepository.findByUsername(userName);
           if(optionalUser.isEmpty()){
               log.error(String.format("Failed to find user with username: %s", userName));
               throw new ResourceNotFoundException("Users","name",userName);
           }
           if(optionalAddress.get().getUserId()!= optionalUser.get().getId()){
              throw new NotAuthorizedException("You can't delete this address");
           }
           return jdbcAddressRepository.remove(id);
        } catch (DaoException e){
            log.error(String.format("Failed to delete address with id: '%s'", id),e);
            throw new ServiceException(e);
        }
    }
}
