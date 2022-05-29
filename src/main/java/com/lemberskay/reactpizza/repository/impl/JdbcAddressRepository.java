package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Address;
import com.lemberskay.reactpizza.repository.AddressRepository;
import com.lemberskay.reactpizza.repository.mapper.AddressRowMapper;
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
public class JdbcAddressRepository implements AddressRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AddressRowMapper addressRowMapper;
    private final String FIND_ALL_SQL = """
            SELECT a.address_id, a.street_name, a.street_number, a.city,a.user_id, c.country_id, c.name as country_name
            FROM addresses AS a
            INNER JOIN countries AS c
            ON a.country_id=c.country_id;
            """;
    private final String FIND_BY_ID_SQL = """
           SELECT a.address_id, a.street_name, a.street_number, a.city,a.user_id, c.country_id, c.name as country_name
           FROM addresses AS a
           INNER JOIN countries AS c 
           ON a.country_id=c.country_id
           WHERE a.address_id = ?;
            """;
    private final String FIND_BY_USER_ID_SQL = """
           SELECT a.address_id, a.street_name, a.street_number, a.city,a.user_id, c.country_id, c.name as country_name
           FROM addresses AS a
           INNER JOIN countries AS c 
           ON a.country_id=c.country_id
           WHERE a.user_id = ?;
            """;
    private final String INSERT_SQL = """
            INSERT INTO addresses (street_name, street_number, city, country_id, user_id)
            VALUES (?, ?, ?, ?,?);
            """;
    private final String UPDATE_SQL = """
            UPDATE addresses SET street_name = ?, street_number = ?, city = ?, country_id = ? user_id = ?
            WHERE address_id = ?
            """;
    private final String DELETE_SQL = """
            DELETE FROM addresses WHERE address_id = ?
             """;

    public  JdbcAddressRepository(JdbcTemplate jdbcTemplate, AddressRowMapper addressRowMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.addressRowMapper = addressRowMapper;
    }
    @Override
    public Optional<Address> findById(long id) throws DaoException {
        try {
            List<Address> result = jdbcTemplate.query(FIND_BY_ID_SQL, this.addressRowMapper::mapRow, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Address insert(Address address) throws DaoException {
        try{
            final PreparedStatementCreator psc = new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement( INSERT_SQL,
                            Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1, address.getStreetName());
                    ps.setInt(2, address.getStreetNumber());
                    ps.setString(3, address.getCity());
                    ps.setLong(4, address.getCountry().getId());
                    ps.setLong(5, address.getUserId());

                    return ps;
                }
            };

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(psc, keyHolder);

            long insertedId = keyHolder.getKey().longValue();
            address.setId(insertedId);
            return address;
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
    public List findAll() throws DaoException {
        try {
            return jdbcTemplate.query(FIND_ALL_SQL, this.addressRowMapper::mapRow);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public Address update(long id, Address address) throws DaoException {
        try{
            jdbcTemplate.update(UPDATE_SQL, address.getStreetName(), address.getStreetNumber(), address.getCity(), address.getCountry().getId(), address.getUserId(), id);
            return address;
        } catch (DataAccessException e){
            throw new DaoException(e);
        }
    }

    @Override
    public List<Address> findByUserId(Long userId) throws DaoException {
        try {
            return jdbcTemplate.query(FIND_BY_USER_ID_SQL, this.addressRowMapper::mapRow, userId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
