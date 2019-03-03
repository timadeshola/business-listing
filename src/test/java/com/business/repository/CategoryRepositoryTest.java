package com.business.repository;

import com.business.jpa.entity.Category;
import com.business.jpa.repository.CategoryRepository;
import org.assertj.core.api.Java6Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @Before
    public void setUp() {
        category = new Category();
    }

    @Test
    public void testCreateCategoryRepository() {
        category = Category.builder().id(1L).name("IT").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        category = categoryRepository.save(category);
        assertThat(category.getName()).isEqualTo("IT");
    }

    @Test
    public void testUpdateCategoryRepository() {
        category = Category.builder().id(1L).name("IT").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        category = categoryRepository.save(category);
        assertThat(category.getName()).isEqualTo("IT");
        Optional<Category> categoryOptional = categoryRepository.findById(1L);
        category = categoryOptional.get();
        category.setName("COMM");
        category = categoryRepository.save(category);
        assertThat(category.getName()).isEqualTo("COMM");
    }

    @Test
    public void fetchFromCategoryRepository() {
        Optional<Category> optionalCategory = categoryRepository.findById(1L);
        if(optionalCategory.isPresent()) {
            category = optionalCategory.get();
            assertThat(category.getName()).isEqualTo("IT");
            assertThat(category.getDateCreated()).isNotNull();
        }
    }

    @Test
    public void deleteCategoryRepository() {
        Optional<Category> optionalCategory = categoryRepository.findById(1L);
        if(optionalCategory.isPresent()) {
            category = optionalCategory.get();
            categoryRepository.delete(category);
            assertThat(category).isNull();
        }
    }
}
