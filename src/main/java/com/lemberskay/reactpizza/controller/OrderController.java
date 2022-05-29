package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.MenuItem;
import com.lemberskay.reactpizza.model.Order;
import com.lemberskay.reactpizza.service.OrderService;
import org.apache.tomcat.util.codec.binary.Base64;
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
        String usernameAndPassword = new String(Base64.decodeBase64(authorization.substring(6)));
        String userName = usernameAndPassword.split(":")[0];
        return orderService.getOrdersByUser(userName);
    }
}
