package com.lemberskay.reactpizza.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Country extends AbstractEntity{
    private String name;
}
