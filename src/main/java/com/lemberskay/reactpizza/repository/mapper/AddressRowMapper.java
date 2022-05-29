package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.Address;
import com.lemberskay.reactpizza.model.Country;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.lemberskay.reactpizza.repository.mapper.ColumnName.*;

@Component
public class AddressRowMapper implements RowMapper<Address> {

    private final CountryRowMapper countryRowMapper;

    private AddressRowMapper(CountryRowMapper countryRowMapper){
        this.countryRowMapper = countryRowMapper;
    }
    @Override
    public Address mapRow(ResultSet row, int rowNum) throws SQLException {
        return Address.builder()
                .id(row.getLong(ADDRESS_ID))
                .streetName(row.getString(ADDRESS_STREET_NAME))
                .streetNumber(row.getInt(ADDRESS_STREET_NUMBER))
                .city(row.getString(ADDRESS_CITY))
                .country(countryRowMapper.mapRow(row,rowNum))
                .build();
    }
}
