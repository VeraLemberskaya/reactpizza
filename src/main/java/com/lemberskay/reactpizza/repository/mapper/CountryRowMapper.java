package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.entity.Country;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.lemberskay.reactpizza.repository.mapper.ColumnName.*;

@Component
public class CountryRowMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet row, int rowNum) throws SQLException {
        return Country.builder()
                .id(row.getLong(COUNTRY_ID))
                .name(row.getString(COUNTRY_NAME))
                .build();
    }
}
