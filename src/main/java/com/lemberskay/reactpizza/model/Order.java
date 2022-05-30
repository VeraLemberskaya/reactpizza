package com.lemberskay.reactpizza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractEntity{

    private LocalDate date;
    private Address address;
    private List<OrderedItem> orderedItems;

    public void addMenuItem(MenuItem menuItem, int quantity){
        orderedItems.add(new OrderedItem(quantity,menuItem));
    }

    public BigDecimal getTotalCost(){
        return orderedItems.stream().map(orderedItem->orderedItem.
                getMenuItem()
                .getPrice()
                .multiply(new BigDecimal(orderedItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
