package com.lemberskay.reactpizza.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address extends AbstractEntity{
    private String streetName;
    private int streetNumber;
    private String city;
    private Country country;

    public Address(){
        super();
    }
    public Address(long id, String streetName, int streetNumber, String city, Country country){
        super(id);
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.city = city;
        this.country = country;
    }
}
