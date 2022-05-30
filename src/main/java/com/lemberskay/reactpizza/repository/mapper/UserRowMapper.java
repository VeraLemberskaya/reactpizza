package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.entity.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import static com.lemberskay.reactpizza.repository.mapper.ColumnName.*;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet row, int rowNum) throws SQLException {
        return User.builder()
                .id(row.getLong(USER_ID))
                .login(row.getString(USER_LOGIN))
                .password(row.getString(USER_PASSWORD))
                .role(User.Role.valueOf(row.getString(USER_ROLE).toUpperCase(Locale.ROOT)))
                .build();
   }
}
