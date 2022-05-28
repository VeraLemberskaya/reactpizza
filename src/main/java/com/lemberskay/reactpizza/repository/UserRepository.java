package com.lemberskay.reactpizza.repository;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User>{
    public Optional<User> findByUsername(String username) throws DaoException;
    public boolean isUserExistByUsername(String username) throws DaoException;
}
