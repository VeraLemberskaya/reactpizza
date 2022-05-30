package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.NotAuthorizedException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.dto.OrderDto;
import com.lemberskay.reactpizza.model.dto.OrderedItemDto;
import com.lemberskay.reactpizza.model.entity.*;
import com.lemberskay.reactpizza.repository.impl.JdbcAddressRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcMenuItemRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcOrderRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcUserRepository;
import com.lemberskay.reactpizza.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final JdbcOrderRepository jdbcOrderRepository;
    private final JdbcUserRepository jdbcUserRepository;
    private final JdbcAddressRepository jdbcAddressRepository;
    private final JdbcMenuItemRepository jdbcMenuItemRepository;

    public OrderServiceImpl(JdbcOrderRepository jdbcOrderRepository, JdbcUserRepository jdbcUserRepository, JdbcAddressRepository jdbcAddressRepository, JdbcMenuItemRepository jdbcMenuItemRepository) {
        this.jdbcOrderRepository = jdbcOrderRepository;
        this.jdbcUserRepository = jdbcUserRepository;
        this.jdbcAddressRepository = jdbcAddressRepository;
        this.jdbcMenuItemRepository = jdbcMenuItemRepository;
    }

    @Override
    public List<Order> getAllOrders() throws ServiceException {
        try {
            return jdbcOrderRepository.findAll();
        } catch (DaoException e) {
            log.error("Failed to get orders from database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Order getOrderById(long id) throws ServiceException {
        try {
            Optional<Order> optionalOrder = jdbcOrderRepository.findById(id);
            if (optionalOrder.isPresent()) {
                return optionalOrder.get();
            } else {
                log.error(String.format("Failed to find order with id: %s", id));
                throw new ResourceNotFoundException("Orders", "id", id);
            }
        } catch (DaoException e) {
            log.error(String.format("Failed to get order with id: %s from database", id), e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public List<Order> getOrdersByUser(String userName) throws ServiceException {
        try {
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(userName);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return jdbcOrderRepository.findByUserId(user.getId());
            } else {
                log.error(String.format("Failed to find user with username: %s", userName));
                throw new ResourceNotFoundException("Users", "username", userName);
            }
        } catch (DaoException e) {
            log.error(String.format("Failed to get order by username: %s from database", userName), e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Order createOrder(OrderDto orderDto) throws ServiceException {
        try {
            Long addressId = orderDto.getAddressId();
            Optional<Address> optionalAddress = jdbcAddressRepository.findById(addressId);
            if (optionalAddress.isEmpty()) {
                throw new ResourceNotFoundException("Addresses", "id", addressId);
            }
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(userName);
            if (optionalUser.isEmpty()) {
                throw new ResourceNotFoundException("Users", "username", userName);
            }
            User user = optionalUser.get();
            List<Address> userAddresses = jdbcAddressRepository.findByUserId(user.getId());
            if (!userAddresses.stream().anyMatch(address -> address.getId() == addressId)) {
                throw new NotAuthorizedException("You can't create order with such address");
            }

            List<OrderedItem> orderedItems = new ArrayList<>();
            for(OrderedItemDto item: orderDto.getOrderedItems()){
                Optional<MenuItem> optinalMenuItem = jdbcMenuItemRepository.findById(item.getItemId());
                if(optinalMenuItem.isEmpty()){
                    throw new ResourceNotFoundException("MenuItems","id",item.getItemId());
                }
                orderedItems.add(OrderedItem.builder()
                        .menuItem(optinalMenuItem.get())
                        .quantity(item.getQuantity())
                        .build());
            }

            Order order = Order.builder()
                    .date(LocalDate.now())
                    .address(optionalAddress.get())
                    .orderedItems(orderedItems)
                    .build();
            log.info("Order is in process of creating");
            return jdbcOrderRepository.insert(order);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
