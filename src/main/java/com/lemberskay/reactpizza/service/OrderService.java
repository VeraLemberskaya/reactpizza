package com.lemberskay.reactpizza.service;

import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders() throws ServiceException;
    Order getOrderById(long id) throws ServiceException;

    List<Order> getOrdersByUser(String userName) throws ServiceException;

    Order createOrder(Order order) throws  ServiceException;
}
