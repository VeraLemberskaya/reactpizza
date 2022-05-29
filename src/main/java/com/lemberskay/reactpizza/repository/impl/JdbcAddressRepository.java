package com.lemberskay.reactpizza.repository.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Address;
import com.lemberskay.reactpizza.repository.AddressRepository;
import com.lemberskay.reactpizza.repository.mapper.AddressRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcAddressRepository implements AddressRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AddressRowMapper addressRowMapper;
    private final String FIND_ALL_SQL = """
            SELECT a.address_id, a.street_name, a.street_number, a.city, c.country_id, c.name FROM addresses AS a INNER JOIN countries AS c ON a.country_id=c.country_id;
            """;
    private final String FIND_BY_ID_SQL = """
           SELECT a.address_id, a.street_name, a.street_number, a.city, c.country_id, c.name FROM addresses AS a INNER JOIN countries AS c ON a.country_id=c.country_id WHERE c.country_id = ?;
            """;
    private final String INSERT_SQL = """
            INSERT INTO addresses (street_name, street_number, city, country_id) VALUES (?, ?, ?, ?);
            """;
    private final String UPDATE_SQL = """
            UPDATE addresses SET street_name = ?, street_number = ?, city = ?, country_id = ?  WHERE address_id = ?
            """;
    private final String DELETE_SQL = """
            DELETE FROM addresses WHERE address_id = ?
             """;

    public  JdbcAddressRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.addressRowMapper = new AddressRowMapper();
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
            jdbcTemplate.update(INSERT_SQL,address.getStreetName(), address.getStreetNumber(),address.getCity(), address.getCountry().getId());
            return address;
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
            jdbcTemplate.update(UPDATE_SQL, address.getStreetName(), address.getStreetNumber(), address.getCity(), address.getCountry().getId(), id);
            return address;
        } catch (DataAccessException e){
            throw new DaoException(e);
        }
    }
}
