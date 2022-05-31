package com.lemberskay.reactpizza.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AddressDto {
    private long id;
    private String streetName;
    private int streetNumber;
    private String city;
    private String countryName;
}
