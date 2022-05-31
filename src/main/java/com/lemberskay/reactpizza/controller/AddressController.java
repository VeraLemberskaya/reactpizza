package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.converter.AddressConverter;
import com.lemberskay.reactpizza.model.dto.AddressDto;
import com.lemberskay.reactpizza.model.entity.Address;
import com.lemberskay.reactpizza.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressConverter addressConverter;
    public AddressController(AddressService addressService, AddressConverter addressConverter){
        this.addressService = addressService;
        this.addressConverter = addressConverter;
    }

    @GetMapping
    public List<AddressDto> getAllAddresses() throws ServiceException {
        List<Address> addresses = addressService.getAllAddresses();
        List<AddressDto> addressDtoList = new ArrayList<>();
        for(Address address : addresses){
            AddressDto addressDto = addressConverter.convertToDto(address);
            addressDtoList.add(addressDto);
        }
        return addressDtoList;
    }

    @GetMapping("/user")
    public List<AddressDto> getAddressByUser() throws ResourceNotFoundException, ServiceException {
        List<Address> addresses = addressService.getAddressesByUser();
        List<AddressDto> addressDtoList = new ArrayList<>();
        for(Address address : addresses){
            AddressDto addressDto = addressConverter.convertToDto(address);
            addressDtoList.add(addressDto);
        }
        return addressDtoList;
    }

    @PostMapping()
    public AddressDto createAddress(@RequestBody AddressDto addressDto) throws ServiceException{
            Address createdAddress = addressService.createAddress(addressConverter.convertToEntity(addressDto));
            return addressConverter.convertToDto(createdAddress);
    }

    @PutMapping("/{id}")
    public AddressDto updateAddress(@PathVariable("id") Long id, @RequestBody AddressDto addressDto) throws ServiceException{
        Address updatedAddress =  addressService.updateAddress(id,addressConverter.convertToEntity(addressDto));
        return addressConverter.convertToDto(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public boolean deleteAddress(@PathVariable("id") Long id) throws ServiceException{
       return addressService.deleteAddress(id);
    }

}
