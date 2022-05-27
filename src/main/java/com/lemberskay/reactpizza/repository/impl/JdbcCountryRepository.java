package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Country;
import com.lemberskay.reactpizza.repository.CountryRepository;
import com.lemberskay.reactpizza.repository.mapper.CountryRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcCountryRepository implements CountryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final CountryRowMapper countryRowMapper;

    private JdbcCountryRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        countryRowMapper = new CountryRowMapper();
    }

    private final String FIND_ALL_SQL = """
            SELECT  country_id, name, img FROM countries
            """;
    private final String FIND_BY_ID_SQL = """
            SELECT country_id, name, FROM categories FROM countries WHERE country_id = ?
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
    public Country insert(Country country) throws DaoException {
        try{
            jdbcTemplate.update(INSERT_SQL, country.getName());
            return country;
        }catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean remove(Country country) throws DaoException {
        try{
            jdbcTemplate.update(DELETE_SQL,country.getId());
            return true;
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
    public Country update(long id, Country country) throws DaoException {
        try{
            jdbcTemplate.update(UPDATE_SQL, country.getName(), id);
            return country;
        } catch (DataAccessException e){
            throw new DaoException(e);
        }
    }
}
