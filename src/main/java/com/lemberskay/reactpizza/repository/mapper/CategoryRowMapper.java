package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.MenuItem;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lemberskay.reactpizza.repository.mapper.ColumnName.*;

@Component
public class CategoryRowMapper implements ResultSetExtractor<List<Category>> {

    private final MenuItemRowMapper menuItemRowMapper;

    private CategoryRowMapper(MenuItemRowMapper menuItemRowMapper){
        this.menuItemRowMapper = menuItemRowMapper;
    }

    @Override
    public List<Category> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Category> categoriesById = new HashMap<>();
        while (rs.next()) {
            Long categoryId = rs.getLong(CATEGORY_ID);
            String categoryName = rs.getString(CATEGORY_NAME);
            String categoryImgURL = rs.getString(CATEGORY_IMG_URL);
            MenuItem menuItem = menuItemRowMapper.mapRow(rs,0);
            Category category = categoriesById.get(categoryId);
            if (category == null) {
                category = Category.builder()
                        .id(categoryId)
                        .name(categoryName)
                        .imgURL(categoryImgURL)
                        .products(new ArrayList<>())
                        .build();
                categoriesById.put(category.getId(), category);
            }
            if(menuItem!=null)category.addProduct(menuItem);
        }
        return new ArrayList<>(categoriesById.values());
    }
}
