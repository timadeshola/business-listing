package com.business.model;

import com.business.jpa.entity.Country;
import com.business.jpa.entity.States;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class StateTest {

    private States state;

    @Before
    public void setUp() {
        state =new States();
    }

    @Test
    public void testStates() {
        state = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        assertEquals("Lagos", state.getName());
    }
}
