package com.lemberskay.reactpizza.repository;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Category;

import java.util.Optional;

public interface CategoryRepository extends BaseRepository<Category>{
    public Optional<Category> findByName(String name) throws DaoException;

    public boolean isCategoryExist(long id) throws DaoException;
}
