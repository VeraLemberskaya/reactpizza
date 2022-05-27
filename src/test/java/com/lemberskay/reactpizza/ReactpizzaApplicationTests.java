package com.lemberskay.reactpizza;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.repository.impl.JdbcCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReactpizzaApplicationTests {

    @Autowired
    private JdbcCategoryRepository repository;

    @Test
    void contextLoads() throws DaoException {
        var list = repository.findAll();
        for (var category : list) {
            System.out.println(category.getName());
        }
    }

}
