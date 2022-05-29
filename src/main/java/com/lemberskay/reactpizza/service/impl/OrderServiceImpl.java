package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Order;
import com.lemberskay.reactpizza.model.User;
import com.lemberskay.reactpizza.repository.impl.JdbcOrderRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcUserRepository;
import com.lemberskay.reactpizza.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final JdbcOrderRepository jdbcOrderRepository;
    private final JdbcUserRepository jdbcUserRepository;

    public OrderServiceImpl(JdbcOrderRepository jdbcOrderRepository, JdbcUserRepository jdbcUserRepository){
        this.jdbcOrderRepository = jdbcOrderRepository;
        this.jdbcUserRepository = jdbcUserRepository;
    }

    @Override
    public List<Order> getAllOrders() throws ServiceException {
        try{
            return jdbcOrderRepository.findAll();
        } catch (DaoException e){
            log.error("Failed to get orders from database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Order getOrderById(long id) throws ServiceException {
        try{
            Optional<Order> optionalOrder = jdbcOrderRepository.findById(id);
            if(optionalOrder.isPresent()){
                return optionalOrder.get();
            }
            else {
                log.error(String.format("Failed to find order with id: %s", id));
                throw new ResourceNotFoundException("Orders","id",id);
            }
        }catch (DaoException e){
            log.error(String.format("Failed to get order with id: %s from database", id), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> getOrdersByUser(String userName) throws ServiceException {
        try{
            Optional<User> optionalUser = jdbcUserRepository.findByUsername(userName);
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                return jdbcOrderRepository.findByUserId(user.getId());
            } else {
                log.error(String.format("Failed to find user with username: %s", userName));
                throw new ResourceNotFoundException("Users","username",userName);
            }
        } catch (DaoException e){
            log.error(String.format("Failed to get order by username: %s from database", userName), e);
            throw new ServiceException(e);
        }
    }
}
