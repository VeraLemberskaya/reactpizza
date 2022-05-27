package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryRowMapper implements RowMapper<Category>{
    @Override
    public  Category mapRow(ResultSet row, int rowNum) throws SQLException{
        return new Category(row.getLong("category_id"),
                row.getString("name"), row.getString("img"));
    }

    public List<Category> mapRows(ResultSet rs) throws SQLException{
        ProductRowMapper productRowMapper = new ProductRowMapper();
        Map<Long, Category> categoriesById = new HashMap<>();
        while (rs.next()) {
            Long categoryId = rs.getLong("category_id");
            String categoryName = rs.getString("category_name");
            String categoryImgURL = rs.getString("category_img");
            Product product = productRowMapper.mapRow(rs,0);
            Category category = categoriesById.get(categoryId);
            if (category == null) {
                category = new Category(categoryId, categoryName,categoryImgURL);
                categoriesById.put(category.getId(), category);
            }
            if(product!=null)category.addProduct(product);
        }
        return new ArrayList<>(categoriesById.values());
    }
}
