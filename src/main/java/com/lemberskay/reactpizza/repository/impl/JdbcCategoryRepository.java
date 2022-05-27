package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Product;
import com.lemberskay.reactpizza.repository.CategoryRepository;
import com.lemberskay.reactpizza.repository.mapper.CategoryRowMapper;
import com.lemberskay.reactpizza.repository.mapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
public class JdbcCategoryRepository implements CategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CategoryRowMapper categoryRowMapper;

    private final String FIND_ALL_SQL = """
            SELECT p.product_id, p.name, p.description, p.price, p.img, p.rating, c.category_id, c.name as category_name, c.img as category_img FROM products AS p INNER JOIN categories as c ON p.category_id = c.category_id;
              """;
    private final String FIND_BY_ID_SQL = """
            SELECT p.product_id, p.name, p.description, p.price, p.img, p.rating, c.category_id, c.name as category_name, c.img as category_img FROM products AS p RIGHT JOIN categories as c ON p.category_id = c.category_id WHERE c.category_id = ?;
            """;
    private final String FIND_BY_NAME_SQL = """
            SELECT category_id, name, img FROM categories WHERE category_name = ?
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

    @Autowired
    public JdbcCategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRowMapper = new CategoryRowMapper();
    }

    @Override
    public List<Category> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_SQL, this.categoryRowMapper::mapRows);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public Category update(long id, Category category) throws DaoException {
        try {
            jdbcTemplate.update(UPDATE_SQL, category.getName(), category.getImgURL(), id);
            return category;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Category> findById(long id) throws DaoException {
        try {
            List<Category> result = jdbcTemplate.query(FIND_BY_ID_SQL, this.categoryRowMapper::mapRows, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Category insert(Category category) throws DaoException {
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
    public boolean remove(Category category) throws DaoException {
        try {
            jdbcTemplate.update(DELETE_SQL, category.getId());
            return true;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Category> findByName(String name) throws DaoException {
        try {
            List<Category> result = jdbcTemplate.query(FIND_BY_NAME_SQL, this.categoryRowMapper::mapRows, name);
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
