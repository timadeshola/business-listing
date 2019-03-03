package com.business.services.impl;

import com.business.core.exceptions.CustomException;
import com.business.jpa.entity.Country;
import com.business.jpa.repository.CountryRepository;
import com.business.model.request.CountryRequest;
import com.business.services.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class CountryServiceImpl implements CountryService {

    private CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country createCountry(CountryRequest request) {
        Optional<Country> countryOptional = countryRepository.findByName(request.getName());
        if (countryOptional.isPresent()) {
            throw new CustomException("Country already exist", HttpStatus.CONFLICT);
        }
        Country country = new Country();
        country.setName(request.getName());
        country.setCountryCode(request.getCountryCode());

        return countryRepository.save(country);
    }

    @Override
    public Page<Country> findAllCountries(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    @Override
    public Country updateCountry(CountryRequest request, Long countryId) {
        Optional<Country> countryOptional = countryRepository.findById(countryId);
        if (countryOptional.isPresent()) {
            Country country = countryOptional.get();
            if (request.getName() != null) {
                country.setName(request.getName());
            }
            if (request.getCountryCode() != null) {
                country.setCountryCode(request.getCountryCode());
            }
            return countryRepository.save(country);
        }
        throw new CustomException("Country does not exist", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Country> findCountryByName(String name) {
        return countryRepository.findByName(name);
    }

    @Override
    @Transactional
    public void deleteCountryById(Long countryId) {
        countryRepository.deleteById(countryId);
    }
}
