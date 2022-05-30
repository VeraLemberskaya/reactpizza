package com.lemberskay.reactpizza.model.entity;

import com.lemberskay.reactpizza.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends AbstractEntity {
    private String streetName;
    private int streetNumber;
    private String city;
    private long countryId;
    private long userId;

}
