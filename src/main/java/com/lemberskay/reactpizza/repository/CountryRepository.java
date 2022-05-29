package com.lemberskay.reactpizza.repository;

import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.model.Country;

import java.util.Optional;

public interface CountryRepository extends BaseRepository<Country>{
    Optional<Country> findByCountryName(String countryName) throws DaoException;
}
