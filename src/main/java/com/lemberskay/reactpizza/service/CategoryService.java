package com.lemberskay.reactpizza.service;

import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories() throws ServiceException;

    Category getCategoryById(long id) throws  ServiceException;

    Category getCategoryByName(String name) throws ServiceException;

    List<Category> getAddToOrderCategories () throws ServiceException;

    Category createCategory(Category category) throws ServiceException;

Category updateCategory(long id, Category category) throws ServiceException;
}
