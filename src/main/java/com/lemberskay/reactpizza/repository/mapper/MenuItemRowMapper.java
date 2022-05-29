package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.MenuItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.lemberskay.reactpizza.repository.mapper.ColumnName.*;
@Component
public class MenuItemRowMapper implements RowMapper<MenuItem> {
    @Override
    public MenuItem mapRow(ResultSet row, int rowNum) throws SQLException {
        long id = row.getLong(MENU_ITEM_ID);
        return id == 0 ? null : MenuItem.builder()
                .id(id)
                .price(row.getBigDecimal(MENU_ITEM_PRICE))
                .name(row.getString(MENU_ITEM_NAME))
                .description(row.getString(MENU_ITEM_DESCRIPTION))
                .imgURL(row.getString(MENU_ITEM_IMG_URL))
                .rating(row.getInt(MENU_ITEM_RATING))
                .categoryId(row.getLong(CATEGORY_ID))
                .build();
    }
}
