package com.lemberskay.reactpizza.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedItemDto {
    private Long itemId;
    private int quantity;
}
