package com.business.services.impl;

import com.business.core.exceptions.CustomException;
import com.business.jpa.entity.Category;
import com.business.jpa.repository.CategoryRepository;
import com.business.model.request.CategoryRequest;
import com.business.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        Optional<Category> countryOptional = categoryRepository.findByName(request.getName());
        if (countryOptional.isPresent()) {
            throw new CustomException("Category already exist", HttpStatus.CONFLICT);
        }
        Category category = new Category();
        category.setName(request.getName());

        return categoryRepository.save(category);
    }

    @Override
    public Page<Category> findAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category updateCategory(CategoryRequest request, Long categoryId) {
        Optional<Category> countryOptional = categoryRepository.findById(categoryId);
        if (countryOptional.isPresent()) {
            Category category = countryOptional.get();
            if (request.getName() != null) {
                category.setName(request.getName());
            }
            return categoryRepository.save(category);
        }
        throw new CustomException("Category does not exist", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
