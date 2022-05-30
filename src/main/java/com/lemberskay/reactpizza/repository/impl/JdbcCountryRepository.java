package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Country;
import com.lemberskay.reactpizza.repository.CountryRepository;
import com.lemberskay.reactpizza.repository.mapper.CountryRowMapper;
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
public class JdbcCountryRepository implements CountryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final CountryRowMapper countryRowMapper;

    public JdbcCountryRepository(JdbcTemplate jdbcTemplate, CountryRowMapper countryRowMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.countryRowMapper = countryRowMapper;
    }

    private final String FIND_ALL_SQL = """
            SELECT  country_id, name as country_name
            FROM countries
            """;
    private final String FIND_BY_ID_SQL = """
            SELECT country_id, name as country_name
            FROM countries
            WHERE country_id = ?
            """;
    private final String FIND_BY_COUNTRY_NAME = """
            SELECT country_id, name as country_name
            FROM countries
            WHERE name = ?
            """;
    private final String INSERT_SQL = """
            INSERT into countries (name) VALUES (?)
            """;
    private final String UPDATE_SQL = """
            UPDATE countries SET name = ? WHERE country_id = ?
            """;
    private final String DELETE_SQL = """
            DELETE FROM countries WHERE country_id = ?
            """;
    @Override
    public Optional<Country> findById(long id) throws DaoException {
        try {
            List<Country> result = jdbcTemplate.query(FIND_BY_ID_SQL, this.countryRowMapper::mapRow, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Country insert(@NotNull Country country) throws DaoException {
        try{
            final PreparedStatementCreator psc = new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement( INSERT_SQL,
                            Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1, country.getName());

                    return ps;
                }
            };

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(psc, keyHolder);

            long insertedId = keyHolder.getKey().longValue();
            country.setId(insertedId);
            return country;
        }catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean remove(long id) throws DaoException {
        try{
            int result = jdbcTemplate.update(DELETE_SQL,id);
            return result!=0;
        }catch( DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public List<Country> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_SQL, this.countryRowMapper::mapRow);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Country update(long id, @NotNull Country country) throws DaoException {
        try{
            jdbcTemplate.update(UPDATE_SQL, country.getName(), id);
            country.setId(id);
            return country;
        } catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Country> findByCountryName(String countryName) throws DaoException {
        try {
            List<Country> result = jdbcTemplate.query(FIND_BY_COUNTRY_NAME, this.countryRowMapper::mapRow, countryName);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
