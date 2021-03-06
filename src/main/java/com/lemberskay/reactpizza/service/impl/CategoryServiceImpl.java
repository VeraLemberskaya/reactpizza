package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.Category;
import com.lemberskay.reactpizza.repository.impl.JdbcCategoryRepository;
import com.lemberskay.reactpizza.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final JdbcCategoryRepository jdbcCategoryRepository;

    public CategoryServiceImpl( JdbcCategoryRepository jdbcCategoryRepository){
        this.jdbcCategoryRepository = jdbcCategoryRepository;
    }

    @Override
    public List<Category> getAllCategories() throws ServiceException {
        try{
        return jdbcCategoryRepository.findAll();
        } catch (DaoException e){
            log.error("Failed to get categories from database", e);
            throw new ServiceException(e);
        }

    }

    @Override
    public Category getCategoryById(long id) throws ServiceException {
        try{
            Optional<Category> category = jdbcCategoryRepository.findById(id);
            if(category.isPresent()){
                return category.get();
            }
            else {
                log.error(String.format("Failed to find category with id: %s", id));
                throw new ResourceNotFoundException("Categories","id",id);
            }
        }catch (DaoException e){
            log.error(String.format("Failed to get category with id: %s from database", id), e);
            throw new ServiceException(e);
        }
        
    }

    @Override
    public Category getCategoryByName(String name) throws ServiceException {
        try{
            Optional<Category> category = jdbcCategoryRepository.findByName(name);
            if(category.isPresent()){
                return category.get();
            }
            else {
                log.error(String.format("Failed to find category with name: %s", name));
                throw new ResourceNotFoundException("Categories","name",name);
            }
        }catch (DaoException e){
            log.error(String.format("Failed to get category with name: %s from database", name), e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public List<Category> getAddToOrderCategories () throws ServiceException{
        List<Long> addToOrderCategoryIds = Stream.of(2L,3L,4L)
                .collect(Collectors.toList());
        List<Category> addToOrderCategories = new ArrayList<Category>();

        try{
            for (var id : addToOrderCategoryIds) {
                Optional<Category> optionalCategory = jdbcCategoryRepository.findById(id);
                if(optionalCategory.isPresent()){
                    addToOrderCategories.add(optionalCategory.get());
                }
                else {
                    throw new ResourceNotFoundException("Categories","id",id);
                }
            }
            return addToOrderCategories;
        }catch (DaoException e){
            log.error(String.format("Failed to get categories with ids: %s,&s,%s from database", 2,3,4), e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Category createCategory(Category category) throws ServiceException {
        try{
            log.info("Category is in process of creating...");
            return jdbcCategoryRepository.insert(category);
        }catch(DaoException e){
            log.error("Failed to insert category into database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Category updateCategory(long id, Category category) throws ServiceException {
        try{
            Optional<Category> optionalCategory = jdbcCategoryRepository.findById(id);
            if(optionalCategory.isPresent()){
                log.info("Category is in process of updating...");
                return jdbcCategoryRepository.update(id, category);
            }
            else {
                log.error(String.format("Failed to get category with id: %s", id));
                throw new ResourceNotFoundException("Categories","id",id);
            }

        } catch(DaoException e){
            log.error(String.format("Failed to update category with id: %s in database", id));
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long id) throws ServiceException {
        try{
            boolean isCategoryExists =  jdbcCategoryRepository.isCategoryExist(id);
            if(!isCategoryExists){
                throw new ResourceNotFoundException("Categories","id",id);
            }
            return jdbcCategoryRepository.remove(id);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}
