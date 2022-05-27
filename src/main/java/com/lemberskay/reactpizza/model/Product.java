package com.lemberskay.reactpizza.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product extends AbstractEntity{
    private BigDecimal price;
    private String description;
    private String name;
    private String imgURL;
    private int rating;

    private long categoryId;

    public Product(){super();}
    public Product(long id, BigDecimal price, String description, String name, String imgURL, int rating, long categoryId){
        super(id);
        this.price = price;
        this.description = description;
        this.name = name;
        this.imgURL = imgURL;
        this.rating = rating;
        this.categoryId = categoryId;
    }
}
