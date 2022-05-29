package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.Address;
import com.lemberskay.reactpizza.model.Country;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressRowMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet row, int rowNum) throws SQLException {
        return new Address(row.getLong("address_id"), row.getString("street_name"),
                row.getInt("street_number"),row.getString("city"),new Country(row.getLong("country_id"), row.getString("name")));

    }
}
