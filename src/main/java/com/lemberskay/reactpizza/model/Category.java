package com.lemberskay.reactpizza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends AbstractEntity{
    private String name;
    private String imgURL;
    private List<MenuItem> products;
    public void addProduct(MenuItem product){
        products.add(product);
    }
}
