package com.lemberskay.reactpizza.model.dto;

import com.lemberskay.reactpizza.model.entity.OrderedItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private Long addressId;
    private List<OrderedItemDto> orderedItems;
}
