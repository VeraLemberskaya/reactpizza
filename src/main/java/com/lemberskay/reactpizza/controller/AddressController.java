package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Address;
import com.lemberskay.reactpizza.model.Order;
import com.lemberskay.reactpizza.service.AddressService;
import com.lemberskay.reactpizza.util.UserEncoder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Address> getOrdersByUser(@RequestHeader("Authorization") String authorization) throws ResourceNotFoundException, ServiceException {
        String userName = UserEncoder.getUserName(authorization);
        return addressService.getAddressesByUser(userName);
    }
}
