package com.lemberskay.reactpizza.controller;

import com.lemberskay.reactpizza.exception.AlreadyExistsException;
import com.lemberskay.reactpizza.exception.ServiceException;
import com.lemberskay.reactpizza.model.entity.Country;
import com.lemberskay.reactpizza.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryService countryService;
    public CountryController(CountryService countryService){
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getAllCountries() throws ServiceException {
        return countryService.getAllCountries();
    }

    @PostMapping()
    public Country createCountry(@RequestBody Country country)  throws AlreadyExistsException,ServiceException{
        return countryService.createCountry(country);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCountry(@PathVariable("id") Long id) throws ServiceException{
        return  countryService.deleteCountry(id);
    }

    @PutMapping("/{id}")
    public Country updateCountry(@PathVariable("id") Long id, @RequestBody Country country) throws ServiceException{
        return countryService.updateCountry(id,country);
    }
}
