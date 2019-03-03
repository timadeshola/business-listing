package com.business.repository;

import com.business.jpa.entity.*;
import com.business.jpa.repository.*;
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
public class BusinessRepositoryTest {

    @Autowired private BusinessRepository businessRepository;
    @Autowired private StateRepository stateRepository;
    @Autowired private CountryRepository countryRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private CategoryRepository categoryRepository;

    private Business business;

    @Before
    public void setUp() {
        business = new Business();
    }

    @Test
    public void testCreateBusinessRepository() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        stateRepository.save(states);
        Country country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        countryRepository.save(country);
        Address address = Address.builder().id(1L).houseNo("No. 2").street("Allen Avenue").city("Ikeja").State(states).country(country).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        addressRepository.save(address);
        Category category = Category.builder().id(1L).name("IT").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        categoryRepository.save(category);
        business = Business.builder().id(1L).name("MTN Nigeria").description("MTN Nigeria").address(address).categories(Collections.singleton(category)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        business = businessRepository.save(business);
        assertThat(business.getName()).isEqualTo("MTN Nigeria");
    }

    @Test
    public void testUpdateBusinessRepository() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        stateRepository.save(states);
        Country country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        countryRepository.save(country);
        Address address = Address.builder().id(1L).houseNo("No. 2").street("Allen Avenue").city("Ikeja").State(states).country(country).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        addressRepository.save(address);
        Category category = Category.builder().id(1L).name("IT").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        categoryRepository.save(category);
        business = Business.builder().id(1L).name("MTN Nigeria").description("MTN Nigeria").address(address).categories(Collections.singleton(category)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        business = businessRepository.save(business);
        assertThat(business.getName()).isEqualTo("MTN Nigeria");
        Optional<Business> countryOptional = businessRepository.findByName("MTN Nigeria");
        business = countryOptional.get();
        business.setName("Communication Tech");
        business = businessRepository.save(business);
        assertThat(business.getName()).isEqualTo("Communication Tech");
    }

    @Test
    public void fetchFromBusinessRepository() {
        Optional<Business> optionalCountry = businessRepository.findByName("IT");
        if(optionalCountry.isPresent()) {
            business = optionalCountry.get();
            assertThat(business.getName()).isEqualTo("IT");
            assertThat(business.getDateCreated()).isNotNull();
        }
    }

    @Test
    public void deleteBusinessRepository() {
        Optional<Business> optionalCountry = businessRepository.findByName("IT");
        if(optionalCountry.isPresent()) {
            business = optionalCountry.get();
            businessRepository.delete(business);
            assertThat(business).isNull();
        }
    }
}
