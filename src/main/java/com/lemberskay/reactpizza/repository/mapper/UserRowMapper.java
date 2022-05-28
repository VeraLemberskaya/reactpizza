package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.User;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

@Component
public class UserRowMapper implements RowMapper<User>{
    @Override
    public User mapRow(ResultSet row, int rowNum) throws SQLException {
        return User.builder()
                .id(row.getLong("user_id"))
                .login(row.getString("user_name"))
                .password(row.getString("password"))
                .role(User.Role.valueOf(row.getString("user_role").toUpperCase(Locale.ROOT)))
                .build();
   }
}
