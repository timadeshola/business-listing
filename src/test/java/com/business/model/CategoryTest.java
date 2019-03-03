package com.business.model;

import com.business.jpa.entity.Category;
import com.business.jpa.entity.States;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class CategoryTest {

    private Category category;

    @Before
    public void setUp() {
        category =new Category();
    }

    @Test
    public void testCategory() {
        category = Category.builder().id(1L).name("IT").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        assertEquals("IT", category.getName());
    }
}
