package com.lemberskay.reactpizza.service;

import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.MenuItem;

import java.util.List;

public interface MenuItemService {
    List<MenuItem> getAllMenuItems() throws ServiceException;
    MenuItem getMenuItemById(long id) throws ServiceException;
    List<MenuItem> getMenuItemsByCategory(long category_id) throws ServiceException;
    MenuItem createMenuItem(MenuItem product) throws ServiceException;
    MenuItem updateMenuItem(long id, MenuItem product) throws ServiceException;
}
