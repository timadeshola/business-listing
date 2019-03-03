package com.business.model;

import com.business.jpa.entity.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AddressTest {

    private Address address;

    @Before
    public void setUp() {
        address =new Address();
    }

    @Test
    public void testAddress() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        Country country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        address = Address.builder().id(1L).houseNo("No. 2").street("Allen Avenue").city("Ikeja").State(states).country(country).dateCreated(new Timestamp(System.currentTimeMillis())).build();

        assertEquals("Allen Avenue", address.getStreet());
    }
}
