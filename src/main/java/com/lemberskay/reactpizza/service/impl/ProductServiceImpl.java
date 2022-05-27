package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Product;
import com.lemberskay.reactpizza.repository.impl.JdbcCategoryRepository;
import com.lemberskay.reactpizza.repository.impl.JdbcProductRepository;
import com.lemberskay.reactpizza.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private JdbcProductRepository jdbcProductRepository;
    private JdbcCategoryRepository jdbcCategoryRepository;

    public ProductServiceImpl(JdbcProductRepository jdbcProductRepository, JdbcCategoryRepository jdbcCategoryRepository){
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcCategoryRepository = jdbcCategoryRepository;
    }

    @Override
    public List<Product> getAllProducts() throws ServiceException {
        try{
            return jdbcProductRepository.findAll();
        } catch ( DaoException e){
            log.error("Failed to get products", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Product getProductById(long id) throws ServiceException {
        try{
            Optional<Product> optionalProduct = jdbcProductRepository.findById(id);
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
    public List<Product> getProductsByCategory(long categoryId) throws ServiceException {
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
    public Product createProduct(Product product) throws ServiceException {
        try{
            return jdbcProductRepository.insert(product);
        }catch(DaoException e){
            log.error("Failed to insert product in database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Product updateProduct(long id, Product product) throws ServiceException {
        try{
            Optional<Product> optionalCategory = jdbcProductRepository.findById(id);
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
