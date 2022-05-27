package com.lemberskay.reactpizza.service;

import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts() throws ServiceException;
    Product getProductById(long id) throws ServiceException;
    List<Product> getProductsByCategory(long category_id) throws ServiceException;
    Product createProduct(Product product) throws ServiceException;
    Product updateProduct(long id, Product product) throws ServiceException;
}
