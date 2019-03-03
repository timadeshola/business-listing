package com.business.services;

import com.business.jpa.entity.Country;
import com.business.model.request.CountryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CountryService {

    Country createCountry(CountryRequest request);

    Page<Country> findAllCountries(Pageable pageable);

    Country updateCountry(CountryRequest request, Long countryId);

    Optional<Country> findCountryByName(String name);

    void deleteCountryById(Long countryId);

}
