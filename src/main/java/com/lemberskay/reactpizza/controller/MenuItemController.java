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
    public List<MenuItem> getAllMenuItems() throws ServiceException {
        return menuItemService.getAllMenuItems();
    }

    @GetMapping(params = "category_id")
    public List<MenuItem> getMenuItemsByCategory(@RequestParam("category_id") long categoryId) throws ResourceNotFoundException, ServiceException {
        return menuItemService.getMenuItemsByCategory(categoryId);
    }

    @GetMapping("/{id}")
    public MenuItem getMenuItemById(@PathVariable("id") long productId) throws ResourceNotFoundException, ServiceException {
        return menuItemService.getMenuItemById(productId);
    }

    @PostMapping
    public MenuItem createMenuItem(@RequestBody MenuItem product) throws ServiceException {
        return menuItemService.createMenuItem(product);
    }

    @PutMapping("/{id}")
    public MenuItem updateMenuItem(@PathVariable("id") Long productId, @RequestBody MenuItem productDetails) throws ResourceNotFoundException, ServiceException {
        return menuItemService.updateMenuItem(productId, productDetails);
    }
}