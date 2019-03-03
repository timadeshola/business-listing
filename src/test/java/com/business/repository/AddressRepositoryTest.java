package com.business.repository;

import com.business.jpa.entity.Address;
import com.business.jpa.entity.Country;
import com.business.jpa.entity.States;
import com.business.jpa.repository.AddressRepository;
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
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired private StateRepository stateRepository;
    @Autowired private CountryRepository countryRepository;

    private Address address;

    @Before
    public void setUp() {
        address = new Address();
    }

    @Test
    public void testCreateAddressRepository() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        stateRepository.save(states);
        Country country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        countryRepository.save(country);
        address = Address.builder().id(1L).houseNo("No. 2").street("Allen Avenue").city("Ikeja").State(states).country(country).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        address = addressRepository.save(address);
        assertThat(address.getCity()).isEqualTo("Ikeja");
    }

    @Test
    public void testUpdateAddressRepository() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        stateRepository.save(states);
        Country country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        countryRepository.save(country);
        address = Address.builder().id(1L).houseNo("No. 2").street("Allen Avenue").city("Ikeja").State(states).country(country).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        address = addressRepository.save(address);
        assertThat(address.getCity()).isEqualTo("Ikeja");
        Optional<Address> addressOptional = addressRepository.findByHouseNo("No. 2");
        address = addressOptional.get();
        address.setHouseNo("Plot 2");
        address = addressRepository.save(address);
        assertThat(address.getHouseNo()).isEqualTo("Plot 2");
    }

    @Test
    public void fetchFromAddressRepository() {
        Optional<Address> addressOptional = addressRepository.findByHouseNo("No. 2");
        if (addressOptional.isPresent()) {
            address = addressOptional.get();
            assertThat(address.getHouseNo()).isEqualTo("Plot 2");
            assertThat(address.getDateCreated()).isNotNull();
        }
    }

    @Test
    public void deleteAddressRepository() {
        Optional<Address> optionalCountry = addressRepository.findById(1L);
        if (optionalCountry.isPresent()) {
            address = optionalCountry.get();
            addressRepository.delete(address);
            assertThat(address).isNotNull();
        }
    }
}
