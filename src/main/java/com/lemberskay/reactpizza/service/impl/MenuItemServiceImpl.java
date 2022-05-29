package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.MenuItem;
import com.lemberskay.reactpizza.repository.impl.JdbcCategoryRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcMenuItemRepository;
import com.lemberskay.reactpizza.service.MenuItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public List<MenuItem> getAllProducts() throws ServiceException {
        try{
            return jdbcProductRepository.findAll();
        } catch ( DaoException e){
            log.error("Failed to get products", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public MenuItem getProductById(long id) throws ServiceException {
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
    public List<MenuItem> getProductsByCategory(long categoryId) throws ServiceException {
        try{
            //to do
            // check id the category exists and throe resource not found exception
            boolean isCategoryExists = jdbcCategoryRepository.isCategoryExist(categoryId);
            if(isCategoryExists){
                return jdbcProductRepository.findProductsByCategory(categoryId);
            } else {
                throw new ResourceNotFoundException("Categories", "id",categoryId);
            }

        } catch (DaoException e){
            log.error(String.format("Failed to get products from database with category_id: %s", categoryId), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public MenuItem createProduct(MenuItem product) throws ServiceException {
        try{
            return jdbcProductRepository.insert(product);
        }catch(DaoException e){
            log.error("Failed to insert product in database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public MenuItem updateProduct(long id, MenuItem product) throws ServiceException {
        try{
            Optional<MenuItem> optionalCategory = jdbcProductRepository.findById(id);
            if(optionalCategory.isPresent()){
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
}
