package com.lemberskay.reactpizza.repository;

import com.lemberskay.reactpizza.model.AbstractEntity;
import com.lemberskay.reactpizza.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<E extends AbstractEntity>{
    public Optional<E> findById(long id) throws DaoException;
    public E insert(E e) throws DaoException;
    public boolean remove(E e) throws DaoException;
    public List<E> findAll() throws DaoException;
    public E update(long id, E e) throws DaoException;
}
