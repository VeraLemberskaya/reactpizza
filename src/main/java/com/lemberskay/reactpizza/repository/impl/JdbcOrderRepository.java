package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Order;
import com.lemberskay.reactpizza.repository.OrderRepository;
import com.lemberskay.reactpizza.repository.mapper.OrderRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final OrderRowMapper orderRowMapper;

    private final String FIND_ALL_SQL = """
           SELECT o.order_id,o.order_date, 
           a.address_id,a.user_id, a.street_name, a.street_number, a.city,
           c.country_id, c.name as country_name,
           m.menu_item_id,m.price,m.description,m.name as menu_item_name,m.img as menu_item_img,m.rating,m.category_id,
           om.quantity 
           FROM orders as o 
           INNER JOIN addresses as a ON a.address_id = o.address_id
           INNER JOIN countries as c ON a.country_id = c.country_id 
           INNER JOIN order_menu_item as om ON o.order_id = om.order_id
           INNER JOIN menu_items as m ON m.menu_item_id = om.menu_item_id;
           """;

    private final String FIND_BY_ID_SQL = """
           SELECT o.order_id,o.order_date, 
           a.address_id,a.user_id, a.street_name, a.street_number, a.city,
           c.country_id, c.name as country_name,
           m.menu_item_id,m.price,m.description,m.name as menu_item_name,m.img as menu_item_img,m.rating,m.category_id,
           om.quantity 
           FROM orders as o 
           INNER JOIN addresses as a ON a.address_id = o.address_id
           INNER JOIN countries as c ON a.country_id = c.country_id 
           INNER JOIN order_menu_item as om ON o.order_id = om.order_id
           INNER JOIN menu_items as m ON m.menu_item_id = om.menu_item_id
           WHERE o.order_id = ?
           """;

    private final String FIND_BY_USER_ID_SQL = """
           SELECT o.order_id,o.order_date, 
           a.address_id,a.user_id, a.street_name, a.street_number, a.city,
           c.country_id, c.name as country_name,
           m.menu_item_id,m.price,m.description,m.name as menu_item_name,m.img as menu_item_img,m.rating,m.category_id,
           om.quantity 
           FROM orders as o 
           INNER JOIN addresses as a ON a.address_id = o.address_id
           INNER JOIN countries as c ON a.country_id = c.country_id 
           INNER JOIN order_menu_item as om ON o.order_id = om.order_id
           INNER JOIN menu_items as m ON m.menu_item_id = om.menu_item_id
           WHERE a.user_id = ?
           """;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate, OrderRowMapper orderRowMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.orderRowMapper = orderRowMapper;
    }

    @Override
    public Optional<Order> findById(long id) throws DaoException {
        try {
            List<Order> result = jdbcTemplate.query(FIND_BY_ID_SQL, this.orderRowMapper::extractData, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Order insert(Order order) throws DaoException {
        return null;
    }

    @Override
    public boolean remove(long id) throws DaoException {
        return false;
    }

    @Override
    public List<Order> findAll() throws DaoException {
        try{
            return jdbcTemplate.query(FIND_ALL_SQL, orderRowMapper::extractData);
        } catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Order update(long id, Order order) throws DaoException {
        return null;
    }

    @Override
    public List<Order> findByUserId(Long userId) throws DaoException {
        try {
            return jdbcTemplate.query(FIND_BY_USER_ID_SQL, this.orderRowMapper::extractData, userId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
