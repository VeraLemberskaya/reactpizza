package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.Country;
import com.lemberskay.reactpizza.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryRowMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet row, int rowNum) throws SQLException {
        return new Country(row.getLong("country_id"),
                row.getString("name"));
    }
}
