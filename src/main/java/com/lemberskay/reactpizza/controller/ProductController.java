package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Product;
import com.lemberskay.reactpizza.service.CategoryService;
import com.lemberskay.reactpizza.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        try{
            return productService.getAllProducts();
        } catch( ServiceException e ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(),e);
        }
    }

    @GetMapping(params = "category_id")
    public List<Product> getProductByCategory(@RequestParam("category_id") long categoryId){
        try{
            return productService.getProductsByCategory(categoryId);
        }catch(ResourceNotFoundException e){
            throw e;
        }catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") long productId) {
        try{
            return productService.getProductById(productId);
        }
        catch(ResourceNotFoundException e){
            throw e;
        } catch(ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        try {
            return productService.createProduct(product);
        } catch( ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
        }
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Long productId, @RequestBody Product productDetails) {

        try{
            return productService.updateProduct(productId, productDetails);
        }
        catch(ResourceNotFoundException e){
            throw e;
        }
        catch(ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }
}
