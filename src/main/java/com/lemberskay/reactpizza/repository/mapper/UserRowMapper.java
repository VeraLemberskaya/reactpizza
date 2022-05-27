package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User>{
    @Override
    public User mapRow(ResultSet row, int rowNum) throws SQLException {
        return new User(row.getLong("user_id"),
                row.getString("name"), row.getString("email"),row.getString("password"));
    }
}
