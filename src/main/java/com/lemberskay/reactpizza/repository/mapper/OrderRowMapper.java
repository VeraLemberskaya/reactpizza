package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.entity.Address;
import com.lemberskay.reactpizza.model.entity.MenuItem;
import com.lemberskay.reactpizza.model.entity.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lemberskay.reactpizza.repository.mapper.ColumnName.*;

@Component
public class OrderRowMapper implements ResultSetExtractor<List<Order>> {

    private final MenuItemRowMapper menuItemRowMapper;
    private final AddressRowMapper addressRowMapper;

    private OrderRowMapper(MenuItemRowMapper menuItemRowMapper, AddressRowMapper addressRowMapper){
        this.menuItemRowMapper = menuItemRowMapper;
        this.addressRowMapper = addressRowMapper;
    }
    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Long, Order> ordersById = new HashMap<>();
        while (rs.next()){
            Long orderId = rs.getLong(ORDER_ID);
            LocalDate orderDate = rs.getDate(ORDER_DATE).toLocalDate();
            Address orderAddress = addressRowMapper.mapRow(rs,0);
            MenuItem menuItem = menuItemRowMapper.mapRow(rs,0);
            int menuItemQuantity = rs.getInt(ORDER_MENU_ITEM_QUANTITY);
            Order order = ordersById.get(orderId);
            if(order == null){
                order = Order.builder()
                        .id(orderId)
                        .date(orderDate)
                        .address(orderAddress)
                        .orderedItems(new ArrayList<>())
                        .build();
                        ordersById.put(order.getId(), order);
            }
            if(menuItem != null) order.addMenuItem(menuItem, menuItemQuantity);
        }

        return new ArrayList<>(ordersById.values());
    }
}
