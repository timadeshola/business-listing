package com.business.model;

import com.business.jpa.entity.Role;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class RoleTest {

    private Role role;

    @Before
    public void setUp() {
        role =new Role();
    }

    @Test
    public void testRole() {
        role = Role.builder().id(1L).name("Administrator").dateCreated(new Timestamp(System.currentTimeMillis())).build();

        assertEquals("Administrator", role.getName());
    }
}
