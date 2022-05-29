package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.MenuItem;
import com.lemberskay.reactpizza.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {
    private MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public List<MenuItem> getAllProducts() throws ServiceException {
        return menuItemService.getAllProducts();
    }

    @GetMapping(params = "category_id")
    public List<MenuItem> getProductByCategory(@RequestParam("category_id") long categoryId) throws ResourceNotFoundException, ServiceException {
        return menuItemService.getProductsByCategory(categoryId);
    }

    @GetMapping("/{id}")
    public MenuItem getProductById(@PathVariable("id") long productId) throws ResourceNotFoundException, ServiceException {
        return menuItemService.getProductById(productId);
    }

    @PostMapping
    public MenuItem createProduct(@RequestBody MenuItem product) throws ServiceException {
        return menuItemService.createProduct(product);
    }

    @PutMapping("/{id}")
    public MenuItem updateProduct(@PathVariable("id") Long productId, @RequestBody MenuItem productDetails) throws ResourceNotFoundException, ServiceException {
        return menuItemService.updateProduct(productId, productDetails);
    }
}