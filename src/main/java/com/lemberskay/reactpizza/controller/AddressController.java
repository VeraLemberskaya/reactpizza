package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Address;
import com.lemberskay.reactpizza.model.Order;
import com.lemberskay.reactpizza.service.AddressService;
import com.lemberskay.reactpizza.util.UserEncoder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressService addressService;
    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @GetMapping
    public List<Address> getAllAddresses() throws ServiceException {
        return addressService.getAllAddresses();
    }

    @GetMapping("/user")
    public List<Address> getAddressByUser() throws ResourceNotFoundException, ServiceException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.getAddressesByUser(userName);
    }

    @PostMapping()
    public Address createAddress(@RequestBody Address address) throws ServiceException{
            return addressService.createAddress(address);
    }

    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable("id") Long id, @RequestBody Address address) throws ServiceException{
        return addressService.updateAddress(id,address);
    }

    @DeleteMapping("/{id}")
    public boolean deleteAddress(@PathVariable("id") Long id) throws ServiceException{
       return addressService.deleteAddress(id);
    }

}
