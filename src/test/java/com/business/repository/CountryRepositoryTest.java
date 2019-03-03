package com.business.repository;

import com.business.jpa.entity.Country;
import com.business.jpa.entity.Country;
import com.business.jpa.entity.States;
import com.business.jpa.repository.CountryRepository;
import com.business.jpa.repository.StateRepository;
import org.assertj.core.api.Java6Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class CountryRepositoryTest {

    @Autowired private CountryRepository countryRepository;
    @Autowired private StateRepository stateRepository;

    private Country country;

    @Before
    public void setUp() {
        country = new Country();
    }

    @Test
    public void testCreateCountryRepository() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        stateRepository.save(states);
        Country country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        country = countryRepository.save(country);
        assertThat(country.getName()).isEqualTo("Nigeria");
    }

    @Test
    public void testUpdateCountryRepository() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        stateRepository.save(states);
        Country country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        country = countryRepository.save(country);
        assertThat(country.getName()).isEqualTo("Nigeria");
        Optional<Country> countryOptional = countryRepository.findByName("Nigeria");
        country = countryOptional.get();
        country.setName("Ghana");
        country = countryRepository.save(country);
        assertThat(country.getName()).isEqualTo("Ghana");
    }

    @Test
    public void fetchFromCountryRepository() {
        Optional<Country> countryOptional = countryRepository.findById(1L);
        if (countryOptional.isPresent()) {
            country = countryOptional.get();
            assertThat(country.getName()).isEqualTo("Nigeria");
            assertThat(country.getDateCreated()).isNotNull();
        }
    }

    @Test
    public void deleteCountryRepository() {
        Optional<Country> optionalCountry = countryRepository.findById(1L);
        if (optionalCountry.isPresent()) {
            country = optionalCountry.get();
            countryRepository.delete(country);
            assertThat(country).isNotNull();
        }
    }
}
