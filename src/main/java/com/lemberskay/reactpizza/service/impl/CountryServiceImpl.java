package com.lemberskay.reactpizza.service.impl;

import com.lemberskay.reactpizza.exception.AlreadyExistsException;
import com.lemberskay.reactpizza.exception.DaoException;
import com.lemberskay.reactpizza.exception.ResourceNotFoundException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.Country;
import com.lemberskay.reactpizza.repository.impl.JdbcCountryRepository;
import com.lemberskay.reactpizza.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CountryServiceImpl implements CountryService {

    private JdbcCountryRepository jdbcCountryRepository;

    public CountryServiceImpl(JdbcCountryRepository jdbcCountryRepository){
        this.jdbcCountryRepository = jdbcCountryRepository;
    }

    @Override
    public List<Country> getAllCountries() throws ServiceException {
        try{
            return jdbcCountryRepository.findAll();
        } catch (DaoException e){
            log.error("Failed to get countries from database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Country createCountry(Country country) throws ServiceException {
       try{
           Optional<Country> optionalCountry = jdbcCountryRepository.findByCountryName(country.getName());
           if(optionalCountry.isPresent()){
               log.error(String.format("Country with name: '%s' already exists", country.getName()));
               throw new AlreadyExistsException("Country", "name", country.getName());
           }
           log.info("Country is in process of creating");
           return jdbcCountryRepository.insert(country);
       } catch (DaoException e){
           log.error("Failed to insert country into database", e);
           throw new ServiceException(e);
       }
    }

    @Override
    @Transactional
    public boolean deleteCountry(long id) throws ServiceException {
        try {
            Optional<Country> optionalCountry = jdbcCountryRepository.findById(id);
            if(optionalCountry.isEmpty()){
                log.error(String.format("Failed to get country with id: %s", id));
                throw new ResourceNotFoundException("Countries","id",id);
            }
            log.info("Country is in process of deleting...");
            return jdbcCountryRepository.remove(id);
        } catch (DaoException e){
            log.error("Failed to delete country from database", e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Country updateCountry(long id, Country country) throws ServiceException {
        try{
            Optional<Country> optionalCountry = jdbcCountryRepository.findById(id);
            if(optionalCountry.isEmpty()){
                throw new ResourceNotFoundException("Countries","id",id);
            }
            log.info("Country is in process of updating");
            return jdbcCountryRepository.update(id,country);
        } catch (DaoException e){
            throw new ServiceException(e);
        }

    }
}
