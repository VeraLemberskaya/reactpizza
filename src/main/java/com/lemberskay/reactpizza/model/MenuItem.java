package com.lemberskay.reactpizza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem extends AbstractEntity{

    private BigDecimal price;
    private String description;
    private String name;
    private String imgURL;
    private int rating;
    private long categoryId;
}
