package com.lemberskay.reactpizza.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class Category extends AbstractEntity{
    private String name;
    private String imgURL;
    private List<MenuItem> products;

    public Category(){
        super();
        products = new ArrayList<>();
    }

    public Category(long id, String name, String imgURL){
        super(id);
        this.name = name;
        this.imgURL = imgURL;
        products = new ArrayList<>();
    }

    public void addProduct(MenuItem product){
        products.add(product);
    }
}
