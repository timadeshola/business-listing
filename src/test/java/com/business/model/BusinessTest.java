package com.business.model;

import com.business.jpa.entity.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class BusinessTest {

    private Business business;

    @Before
    public void setUp() {
        business =new Business();
    }

    @Test
    public void testBusiness() {
        States states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        Country country = Country.builder().id(1L).name("Nigeria").countryCode("NGR").states(Collections.singleton(states)).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        Address address = Address.builder().id(1L).houseNo("No. 2").street("Allen Avenue").city("Ikeja").State(states).country(country).dateCreated(new Timestamp(System.currentTimeMillis())).build();
        Category category = Category.builder().id(1L).name("IT").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        business = Business.builder().id(1L).name("MTN Nigeria").description("MTN NIgeria").address(address).categories(Collections.singleton(category)).dateCreated(new Timestamp(System.currentTimeMillis())).build();

        assertEquals("MTN Nigeria", business.getName());
    }
}
