package com.business.model;

import com.business.jpa.entity.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CountryTest {

    private Country country;

    @Before
    public void setUp() {
        country =new Country();
    }

    @Test
    public void testCountry() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();

        assertEquals("Nigeria", country.getName());
    }
}
