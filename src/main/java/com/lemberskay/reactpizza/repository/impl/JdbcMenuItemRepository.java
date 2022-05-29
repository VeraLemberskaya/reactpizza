package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.MenuItem;
import com.lemberskay.reactpizza.repository.ProductRepository;
import com.lemberskay.reactpizza.repository.mapper.MenuItemRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMenuItemRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MenuItemRowMapper productRowMapper;

    private final String FIND_ALL_SQL = """ 
            SELECT menu_item_id,name as menu_item_name, description, price, img as menu_item_img, rating, category_id
            FROM menu_items
            """;
    private final String FIND_BY_ID_SQL = """
            SELECT menu_item_id,name as menu_item_name, description, price, img as menu_item_img, rating, category_id
            FROM menu_items
            WHERE menu_item_id = ?
            """;
    private final String INSERT_SQL = """
            INSERT INTO menu_items (price, description, name, img, rating, category_id) VALUES (?, ?, ?, ?, ?, ?);
            """;
    private final String UPDATE_SQL = """
            UPDATE menu_items SET name = ?, price = ?, description = ?, img = ?, rating = ?, category_id = ?
            WHERE menu_item_id = ?
            """;
    private final String DELETE_SQL = """
            DELETE FROM menu_items
            WHERE menu_item_id_id = ?
            """;

    private final String FIND_ALL_BY_CATEGORY_SQL = """
            SELECT menu_item_id,name as menu_item_name, description, price, img as menu_item_img, rating, category_id
            FROM menu_items
            WHERE category_id = ?
            """;

    public JdbcMenuItemRepository(JdbcTemplate jdbcTemplate, MenuItemRowMapper menuItemRowMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.productRowMapper = menuItemRowMapper;
    }
    @Override
    public Optional<MenuItem> findById(long id) throws DaoException {
        try {
            List<MenuItem> result = jdbcTemplate.query(FIND_BY_ID_SQL, this.productRowMapper::mapRow, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public MenuItem insert(MenuItem product) throws DaoException {
        try{
            final PreparedStatementCreator psc = new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement( INSERT_SQL,
                            Statement.RETURN_GENERATED_KEYS);

                    ps.setBigDecimal(1, product.getPrice());
                    ps.setString(2, product.getDescription());
                    ps.setString(3, product.getName());
                    ps.setString(4, product.getImgURL());
                    ps.setInt(5, product.getRating());
                    ps.setLong(6, product.getCategoryId());

                    return ps;
                }
            };

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(psc, keyHolder);

            long insertedId = keyHolder.getKey().longValue();
            product.setId(insertedId);
            return product;

        }catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean remove(long id) throws DaoException {
        try{
            jdbcTemplate.update(DELETE_SQL,id);
            return true;
        }catch( DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public List<MenuItem> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_SQL, this.productRowMapper::mapRow);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public MenuItem update(long id, MenuItem product) throws DaoException {
        try{
            jdbcTemplate.update(UPDATE_SQL, product.getName(), product.getPrice(), product.getDescription(), product.getImgURL(), product.getRating(), product.getCategoryId(), id);
            return product;
        } catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public List<MenuItem> findProductsByCategory(long categoryId) throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_BY_CATEGORY_SQL, this.productRowMapper::mapRow,categoryId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
