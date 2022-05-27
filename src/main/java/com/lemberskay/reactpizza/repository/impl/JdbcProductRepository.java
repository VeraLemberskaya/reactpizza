package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Product;
import com.lemberskay.reactpizza.repository.ProductRepository;
import com.lemberskay.reactpizza.repository.mapper.ProductRowMapper;
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
public class JdbcProductRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRowMapper productRowMapper;

    private final String FIND_ALL_SQL = """ 
            SELECT p.product_id, p.name, p.description, p.price, p.img, p.rating, p.category_id FROM products AS p;
            """;
    private final String FIND_BY_ID_SQL = """
            SELECT p.product_id, p.name, p.description, p.price, p.img, p.rating, p.category_id FROM products AS p WHERE p.product_id = ?;
            """;
    private final String INSERT_SQL = """
            INSERT INTO products (price, description, name, img, rating, category_id) VALUES (?, ?, ?, ?, ?, ?);
            """;
    private final String UPDATE_SQL = """
            UPDATE products SET name = ?, price = ?, description = ?, img = ?, rating = ?, category_id = ?  WHERE product_id = ?
            """;
    private final String DELETE_SQL = """
            DELETE FROM products WHERE product_id = ?
            """;

    private final String FIND_ALL_BY_CATEGORY_SQL = """
             SELECT p.product_id, p.name, p.description, p.price, p.img, p.rating, p.category_id FROM products AS p WHERE p.category_id = ?;
            """;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.productRowMapper = new ProductRowMapper();
    }
    @Override
    public Optional<Product> findById(long id) throws DaoException {
        try {
            List<Product> result = jdbcTemplate.query(FIND_BY_ID_SQL, this.productRowMapper::mapRow, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Product insert(Product product) throws DaoException {
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
//            jdbcTemplate.update(INSERT_SQL,product.getPrice(), product.getDescription(), product.getName(), product.getImgURL(), product.getRating(), product.getCategoryId());
//            return product;
        }catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean remove(Product product) throws DaoException {
        try{
            jdbcTemplate.update(DELETE_SQL,product.getId());
            return true;
        }catch( DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public List<Product> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_SQL, this.productRowMapper::mapRow);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public Product update(long id, Product product) throws DaoException {
        try{
            jdbcTemplate.update(UPDATE_SQL, product.getName(), product.getPrice(), product.getDescription(), product.getImgURL(), product.getRating(), product.getCategoryId(), id);
            return product;
        } catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public List<Product> findProductsByCategory(long categoryId) throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_BY_CATEGORY_SQL, this.productRowMapper::mapRow,categoryId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
