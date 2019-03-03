package com.business.resources;

import com.business.jpa.entity.Category;
import com.business.model.request.CategoryRequest;
import com.business.model.response.CategoryResponse;
import com.business.model.response.PaginateResponse;
import com.business.services.CategoryService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/categories")
@Slf4j
@Api(value = "api/v1/categories", description = "Endpoint for category management", tags = "Category Management")
public class CategoryControlller {

    private CategoryService categoryService;
    private ModelMapper modelMapper;

    public CategoryControlller(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ApiOperation(httpMethod = "POST", value = "Resource to create a category", response = CategoryResponse.class, nickname = "createCategory")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! Category created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different category name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);
        CategoryResponse response = modelMapper.map(category, CategoryResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER' )")
    @PutMapping("/{categoryId}")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a category", response = CategoryResponse.class, nickname = "updateCategory")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Category updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Category ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<CategoryResponse> updateCategory(
            @Valid @RequestBody CategoryRequest request,
            @ApiParam(name = "categoryId", value = "Provide Category ID", required = true) @PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.updateCategory(request, categoryId);
        CategoryResponse response = modelMapper.map(category, CategoryResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{name}")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a category by category name", response = CategoryResponse.class, nickname = "findCategoryByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Category"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<CategoryResponse> findCategoryByName(
            @ApiParam(name = "name", value = "Provide Category Name", required = true) @PathVariable(value = "name") String name) {
        Optional<Category> optionalRole = categoryService.findCategoryByName(name);
        if (optionalRole.isPresent()) {
            CategoryResponse response = modelMapper.map(optionalRole.get(), CategoryResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a category", responseReference = "true", nickname = "deleteCategory")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Category deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Category ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteCategory(
            @ApiParam(name = "categoryId", value = "Provide Category ID", required = true)
            @PathVariable(value = "categoryId") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @ApiOperation(httpMethod = "GET", value = "Resource to view all category", response = PaginateResponse.class, nickname = "findAllCountries")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View All Countries"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<PaginateResponse> findAllCountries(@ApiParam(name = "page", value = "default number of page", required = true)
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @ApiParam(name = "size", value = "default size on result set", required = true)
                                                             @RequestParam(value = "size", defaultValue = "5") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateCreated");
        Page<Category> countries = categoryService.findAllCategory(pageable);
        PaginateResponse response = new PaginateResponse();
        response.setContents(countries.getContent());
        response.setTotalElements(countries.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
