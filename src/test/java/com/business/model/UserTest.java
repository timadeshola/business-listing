package com.business.model;

import com.business.jpa.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void testUser() {
        user = User.builder().id(1L).firstName("Mayowa").lastName("Afolabi").username("mayor").password("password").email("mayor@example.com").dateCreated(new Timestamp(System.currentTimeMillis())).build();

        assertEquals("mayor", user.getUsername());
    }
}
