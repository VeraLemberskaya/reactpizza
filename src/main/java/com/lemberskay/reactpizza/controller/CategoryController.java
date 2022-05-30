package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.Category;
import com.lemberskay.reactpizza.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() throws ServiceException{
            return categoryService.getAllCategories();
    }

    @GetMapping("/to_order")
    public List<Category> getAddToOrderCategories() throws ServiceException, ResourceNotFoundException{
            return categoryService.getAddToOrderCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable("id") long categoryId)  throws ServiceException, ResourceNotFoundException{
            return categoryService.getCategoryById(categoryId);
    }

    @PostMapping()
    public Category createCategory(@Valid @RequestBody Category category)  throws ServiceException{
            return categoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable("id") Long categoryId,
                           @Valid @RequestBody Category categoryDetails)  throws ServiceException, ResourceNotFoundException{

            return categoryService.updateCategory(categoryId, categoryDetails);
    }
}
