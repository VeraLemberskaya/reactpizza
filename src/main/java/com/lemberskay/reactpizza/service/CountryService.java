package com.lemberskay.reactpizza.service;

import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.Category;
import com.lemberskay.reactpizza.model.Country;
import com.lemberskay.reactpizza.service.impl.CountryServiceImpl;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries() throws ServiceException;
    Country createCountry(Country country) throws ServiceException;
    boolean deleteCountry(long id) throws ServiceException;

    Country updateCountry(long id, Country country) throws ServiceException;
}
