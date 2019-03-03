package com.business.services;

import com.business.jpa.entity.Category;
import com.business.model.request.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    Category createCategory(CategoryRequest request);

    Page<Category> findAllCategory(Pageable pageable);

    Category updateCategory(CategoryRequest request, Long categoryId);

    Optional<Category> findCategoryByName(String name);

    void deleteCategoryById(Long countryId);

}
