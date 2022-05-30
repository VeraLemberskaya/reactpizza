package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.repository.CategoryRepository;
import com.lemberskay.reactpizza.repository.mapper.CategoryRowMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcCategoryRepository implements CategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CategoryRowMapper categoryRowMapper;

    private final String FIND_ALL_SQL = """
            SELECT m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.img as menu_item_img, m.rating, c.category_id, c.name as category_name, c.img as category_img
            FROM menu_items AS m
            RIGHT JOIN categories as c ON m.category_id = c.category_id;
              """;
    private final String FIND_BY_ID_SQL = """
           SELECT m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.img as menu_item_img, m.rating, c.category_id, c.name as category_name, c.img as category_img
            FROM menu_items AS m
            RIGHT JOIN categories as c ON m.category_id = c.category_id
            WHERE c.category_id = ?;
             """;
    private final String FIND_BY_NAME_SQL = """
           SELECT m.menu_item_id, m.name as menu_item_name, m.description, m.price, m.img as menu_item_img, m.rating, c.category_id, c.name as category_name, c.img as category_img
            FROM menu_items AS m
            RIGHT JOIN categories as c ON m.category_id = c.category_id
            WHERE c.category_name = ?;
           """;
    private final String INSERT_SQL = """
            INSERT into categories (name, img) VALUES (?, ?)
            """;
    private final String UPDATE_SQL = """
            UPDATE categories SET name = ?, img = ? WHERE category_id = ?
            """;
    private final String DELETE_SQL = """
            DELETE FROM categories WHERE category_id = ?
            """;

    private final String SELECT_EXISTS_SQL = """
              SELECT EXISTS (SELECT category_id FROM categories WHERE category_id = ?)
            """;

    public JdbcCategoryRepository(JdbcTemplate jdbcTemplate, CategoryRowMapper categoryRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRowMapper = categoryRowMapper;
    }

    @Override
    public List<Category> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_SQL, this.categoryRowMapper::extractData);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public Category update(long id, @NotNull Category category) throws DaoException {
        try {
            jdbcTemplate.update(UPDATE_SQL, category.getName(), category.getImgURL(), id);
            category.setId(id);
            return category;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Category> findById(long id) throws DaoException {
        try {
            List<Category> result = jdbcTemplate.query(FIND_BY_ID_SQL, this.categoryRowMapper::extractData, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Category insert(@NotNull Category category) throws DaoException {
        try {
            final PreparedStatementCreator psc = new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement( INSERT_SQL,
                            Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1, category.getName());
                    ps.setString(2, category.getImgURL());

                    return ps;
                }
            };

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(psc, keyHolder);

            long insertedId = keyHolder.getKey().longValue();
            category.setId(insertedId);
            return category;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean remove(long id) throws DaoException {
        try {
            int result = jdbcTemplate.update(DELETE_SQL, id);
            return result!=0;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Category> findByName(String name) throws DaoException {
        try {
            List<Category> result = jdbcTemplate.query(FIND_BY_NAME_SQL, this.categoryRowMapper::extractData, name);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isCategoryExist(long id) throws DaoException {
        try{
            return jdbcTemplate.queryForObject(SELECT_EXISTS_SQL, Boolean.class, id);
        }catch(DataAccessException e){
            throw new DaoException(e);
        }
    }
}
