package com.lemberskay.reactpizza.model.converter;

import com.lemberskay.reactpizza.model.dto.AddressDto;
import com.lemberskay.reactpizza.model.entity.Address;
import com.lemberskay.reactpizza.model.entity.Country;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter implements BaseConverter<AddressDto, Address> {

    @Override
    public AddressDto convertToDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .streetName(address.getStreetName())
                .streetNumber(address.getStreetNumber())
                .city(address.getCity())
                .countryName(address.getCountry().getName())
                .build();
    }

    @Override
    public Address convertToEntity(AddressDto addressDto) {
        return Address.builder()
                .streetName(addressDto.getStreetName())
                .streetNumber(addressDto.getStreetNumber())
                .city(addressDto.getCity())
                .country(Country.builder()
                        .name(addressDto.getCountryName())
                        .build())
                .build();
    }
}
