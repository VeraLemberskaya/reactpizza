package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.MenuItem;
import com.lemberskay.reactpizza.repository.impl.JdbcCategoryRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcMenuItemRepository;
import com.lemberskay.reactpizza.service.MenuItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {

    private JdbcMenuItemRepository jdbcProductRepository;
    private JdbcCategoryRepository jdbcCategoryRepository;

    public MenuItemServiceImpl(JdbcMenuItemRepository jdbcProductRepository, JdbcCategoryRepository jdbcCategoryRepository){
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcCategoryRepository = jdbcCategoryRepository;
    }

    @Override
    public List<MenuItem> getAllMenuItems() throws ServiceException {
        try{
            return jdbcProductRepository.findAll();
        } catch ( DaoException e){
            log.error("Failed to get products", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public MenuItem getMenuItemById(long id) throws ServiceException {
        try{
            Optional<MenuItem> optionalProduct = jdbcProductRepository.findById(id);
            if(optionalProduct.isPresent()){
                return optionalProduct.get();
            }
            else {
                log.error(String.format("Failed to find product with id: %s", id));
                throw  new ResourceNotFoundException("Products", "id", id);
            }
        } catch (DaoException e){
            log.error(String.format("Failed to get product with id: %s from database", id), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(long categoryId) throws ServiceException {
        try{
            boolean isCategoryExists = jdbcCategoryRepository.isCategoryExist(categoryId);
            if(isCategoryExists){
                return jdbcProductRepository.findProductsByCategory(categoryId);
            } else {
                log.error(String.format("Failed to find category with id: %s", categoryId));
                throw new ResourceNotFoundException("Categories", "id",categoryId);
            }

        } catch (DaoException e){
            log.error(String.format("Failed to get products from database with category_id: %s", categoryId), e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public MenuItem createMenuItem(MenuItem product) throws ServiceException {
        try{
            boolean isCategoryExists = jdbcCategoryRepository.isCategoryExist(product.getCategoryId());
            if (isCategoryExists){
                log.info("Menu item is in process of creating...");
                return jdbcProductRepository.insert(product);
            } else{
                log.error(String.format("Failed to find category with id: %s", product.getCategoryId()));
                throw new ResourceNotFoundException("Categories", "id", product.getCategoryId());
            }
        }catch(DaoException e){
            log.error("Failed to insert product in database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public MenuItem updateMenuItem(long id, MenuItem product) throws ServiceException {
        try{
            Optional<MenuItem> optionalCategory = jdbcProductRepository.findById(id);
            if(optionalCategory.isPresent()){
                log.info("Menu item is in process of updating...");
                return jdbcProductRepository.update(id, product);
            }
            else {
                log.error(String.format("Failed to find product with id: %s", id));
                throw new ResourceNotFoundException("Products","id",id);
            }

        } catch(DaoException e){
            log.error(String.format("Failed to update product with id: %s in database", id));
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteMenuItem(long id) throws ServiceException {
        try{
            Optional<MenuItem> optionalMenuItem = jdbcProductRepository.findById(id);
            if(optionalMenuItem.isEmpty()){
                throw new ResourceNotFoundException("Menu","id",id);
            }
            return jdbcProductRepository.remove(id);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}
