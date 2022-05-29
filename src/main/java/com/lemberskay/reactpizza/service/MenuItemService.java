package com.lemberskay.reactpizza.service;

import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.MenuItem;

import java.util.List;

public interface MenuItemService {
    List<MenuItem> getAllProducts() throws ServiceException;
    MenuItem getProductById(long id) throws ServiceException;
    List<MenuItem> getProductsByCategory(long category_id) throws ServiceException;
    MenuItem createProduct(MenuItem product) throws ServiceException;
    MenuItem updateProduct(long id, MenuItem product) throws ServiceException;
}
