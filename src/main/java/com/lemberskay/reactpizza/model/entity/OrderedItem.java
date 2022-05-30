package com.lemberskay.reactpizza.model.entity;

import com.lemberskay.reactpizza.model.entity.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderedItem{
    private int quantity;
    private MenuItem menuItem;
}