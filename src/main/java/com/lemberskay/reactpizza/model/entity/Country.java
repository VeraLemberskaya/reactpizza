package com.lemberskay.reactpizza.model.entity;

import com.lemberskay.reactpizza.model.entity.AbstractEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Country extends AbstractEntity {
    private String name;
}
