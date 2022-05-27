package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.User;
import com.lemberskay.reactpizza.repository.UserRepository;
import com.lemberskay.reactpizza.repository.mapper.CategoryRowMapper;
import com.lemberskay.reactpizza.repository.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    private final String FIND_ALL_SQL = """
             SELECT  user, name, email, password FROM users
            """;

    private final String FIND_BY_ID_SQL = """
            SELECT user_id, name, email, password FROM users WHERE user_id = ?
            """;
    private final String INSERT_SQL = """
             INSERT into users (email, password, name) VALUES (?, ?, ?)
            """;
    private final String DELETE_SQL = """
             DELETE FROM users WHERE user_id = ?
            """;
    private final String UPDATE_SQL = """
            UPDATE users SET email = ?, password = ?, name = ? WHERE user_id = ?
            """;

    private final String FIND_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT user_id, name, email, password FROM users WHERE email = ? AND password = ?
            """;
    private static final String SELECT_EXISTS_BY_EMAIL = """
             SELECT EXISTS (SELECT email FROM users WHERE email = ?)
            """;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userRowMapper = new UserRowMapper();
    }

    @Override
    public Optional<User> findById(long id) throws DaoException {
        try {
            List<User> result = jdbcTemplate.query(FIND_BY_ID_SQL, this.userRowMapper::mapRow, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User insert(User user) throws DaoException {
        try {
            jdbcTemplate.update(INSERT_SQL, user.getEmail(), user.getPassword(), user.getName());
            return user;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean remove(User user) throws DaoException {
        try {
            jdbcTemplate.update(DELETE_SQL, user.getId());
            return true;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_SQL, this.userRowMapper::mapRow);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User update(long id, User user) throws DaoException {
        try {
            jdbcTemplate.update(UPDATE_SQL, user.getEmail(), user.getPassword(), user.getName(), id);
            return user;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) throws DaoException {
        try {
            List<User> result = jdbcTemplate.query(FIND_BY_EMAIL_AND_PASSWORD_SQL, this.userRowMapper::mapRow, email, password);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isUserExistByEmail(String email) throws DaoException {
        try{
            return jdbcTemplate.queryForObject(SELECT_EXISTS_BY_EMAIL, Boolean.class, email);
        }catch(DataAccessException e){
            throw new DaoException(e);
        }
    }
}
