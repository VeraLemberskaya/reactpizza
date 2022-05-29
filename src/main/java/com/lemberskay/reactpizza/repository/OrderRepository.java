package com.lemberskay.reactpizza.repository;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Order;

import java.util.List;

public interface OrderRepository extends BaseRepository<Order>{
    public List<Order> findByUserId(Long userId) throws DaoException;
}
