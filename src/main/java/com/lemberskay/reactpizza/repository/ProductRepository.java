package com.lemberskay.reactpizza.repository;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Product;

import java.util.List;

public interface ProductRepository extends BaseRepository<Product>{
    List<Product> findProductsByCategory(long category_id) throws DaoException;
}
