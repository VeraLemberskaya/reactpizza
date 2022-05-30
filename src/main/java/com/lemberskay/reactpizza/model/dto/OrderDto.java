package com.lemberskay.reactpizza.model.dto;

import com.lemberskay.reactpizza.model.entity.OrderedItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderDto {
    private Long addressId;
    private List<OrderedItemDto> orderedItems;
}
