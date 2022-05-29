package com.lemberskay.reactpizza.repository;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.MenuItem;

import java.util.List;

public interface ProductRepository extends BaseRepository<MenuItem>{
    List<MenuItem> findProductsByCategory(long category_id) throws DaoException;
}
