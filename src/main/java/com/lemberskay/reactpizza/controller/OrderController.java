package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.dto.OrderDto;
import com.lemberskay.reactpizza.model.entity.Order;
import com.lemberskay.reactpizza.service.OrderService;
import com.lemberskay.reactpizza.util.UserEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    private OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() throws ServiceException {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable("id") long orderId)  throws ServiceException, ResourceNotFoundException {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user")
    public List<Order> getOrdersByUser(@RequestHeader("Authorization") String authorization) throws ResourceNotFoundException, ServiceException {
        String userName = UserEncoder.getUserName(authorization);
        return orderService.getOrdersByUser(userName);
    }

    @PostMapping()
    public Order createOrder(@RequestBody OrderDto orderDto) throws ServiceException{
        return orderService.createOrder(orderDto);
    }
}
