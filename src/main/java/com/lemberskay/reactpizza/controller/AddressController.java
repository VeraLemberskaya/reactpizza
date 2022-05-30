package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.Address;
import com.lemberskay.reactpizza.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    //ТАК ХОЧУ КУШАТЬ И СПАТЬ
    //А ЕЩЁ ПИСАТЬ ФРОНТ, ЛЮБЛБЮ ФРОНТ...
    //МОЙ ЛЮБИМЫЙ РЕАКТ СКУЧАЕТ БЕЗ МЕНЯ (((
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
