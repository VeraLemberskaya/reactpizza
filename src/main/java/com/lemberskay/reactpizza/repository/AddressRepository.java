package com.lemberskay.reactpizza.repository;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Address;

import java.util.List;

public interface AddressRepository extends BaseRepository<Address>{
    List<Address> findByUserId(Long userId) throws DaoException;
}
