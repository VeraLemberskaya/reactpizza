package com.lemberskay.reactpizza.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Country extends AbstractEntity{
    private String name;

    public Country(){
        super();
    }
    public Country(long id,String countryName){
        super(id);
        this.name = countryName;
    }
}
