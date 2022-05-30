package com.lemberskay.reactpizza.service;

import com.lemberskay.reactpizza.exception.NotAuthorizedException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses() throws ServiceException;
    List<Address> getAddressesByUser(String userName) throws ServiceException;

    Address createAddress(Address address) throws ServiceException;

    Address updateAddress(long id, Address address) throws ServiceException;

    boolean deleteAddress(long id) throws ServiceException, NotAuthorizedException;
}
