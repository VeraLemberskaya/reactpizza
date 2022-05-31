package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.entity.User;
import com.lemberskay.reactpizza.repository.UserRepository;
import com.lemberskay.reactpizza.repository.mapper.UserRowMapper;
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
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    private final String FIND_ALL_SQL = """
            SELECT  user_id, user_name, password, user_role
            FROM users
            """;

    private final String FIND_BY_ID_SQL = """
            SELECT user_id, user_name, password, user_role
            FROM users
            WHERE user_id = ?
            """;
    private final String INSERT_SQL = """
            INSERT into users ( user_name, password, user_role)
            VALUES (?, ?, ?)
            """;
    private final String DELETE_SQL = """
            DELETE FROM users
            WHERE user_id = ?
            """;
    private final String UPDATE_SQL = """
            UPDATE users SET user_name = ?, password = ?
            WHERE user_id = ?
            """;

    private final String FIND_BY_USERNAME_SQL = """
            SELECT user_id, user_name, password, user_role
            FROM users
            WHERE user_name = ?
            """;
    private static final String SELECT_EXISTS_BY_USERNAME_SQL = """
             SELECT EXISTS (SELECT user_name FROM users WHERE user_name = ?)
            """;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
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
    public User insert(@NotNull User user) throws DaoException {
        try {
            final PreparedStatementCreator psc = new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement( INSERT_SQL,
                            Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1, user.getLogin());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getRole().toString());

                    return ps;
                }
            };

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(psc, keyHolder);

            long insertedId = keyHolder.getKey().longValue();
            user.setId(insertedId);
            return user;
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
    public List<User> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_SQL, this.userRowMapper::mapRow);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User update(long id, @NotNull User user) throws DaoException {
        try {
            jdbcTemplate.update(UPDATE_SQL, user.getLogin(), user.getPassword(), id);
            user.setId(id);
            return user;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) throws DaoException {
        try {
            List<User> result = jdbcTemplate.query(FIND_BY_USERNAME_SQL, this.userRowMapper::mapRow, username);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isUserExistByUsername(String username) throws DaoException {
        try{
            return jdbcTemplate.queryForObject(SELECT_EXISTS_BY_USERNAME_SQL, Boolean.class, username);
        }catch(DataAccessException e){
            throw new DaoException(e);
        }
    }
}
