package com.lemberskay.reactpizza.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Order extends AbstractEntity{

    private LocalDateTime date;
    private Address address;
    private User user;
    private List<Product> products;

    public Order(){
        super();
        this.products = new ArrayList<>();
    }

    public Order(long id, Address address, User user){
        super(id);
        this.address = address;
        this.user = user;
        this.date = LocalDateTime.now();
        this.products = new ArrayList<>();
    }

    public Order(long id, Address address, User user, List<Product> products){
        this(id, address, user);
        this.products = products;
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public BigDecimal getTotalPrice(){
        return products.stream().map(product->product.getPrice()).reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
