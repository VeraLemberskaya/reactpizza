package com.lemberskay.reactpizza.repository.mapper;

import com.lemberskay.reactpizza.model.AbstractEntity;
import com.lemberskay.reactpizza.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<E extends AbstractEntity> {
    public  E mapRow(ResultSet rs, int rowNum) throws SQLException;
}
