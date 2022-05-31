package com.lemberskay.reactpizza.model.converter;

import com.lemberskay.reactpizza.model.entity.AbstractEntity;

public interface BaseConverter<D, E extends AbstractEntity> {
    D convertToDto(E e);
    E convertToEntity(D d);
}
