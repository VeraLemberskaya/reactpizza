package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.AbstractEntity;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product>{
    @Override
    public Product mapRow(ResultSet row, int rowNum) throws SQLException {
        long id = row.getLong("product_id");
        return id == 0 ? null : new Product(row.getLong("product_id"),row.getBigDecimal("price"), row.getString("description"),
                row.getString("name"),row.getString("img"), row.getInt("rating"), row.getLong("category_id"));
    }
}
