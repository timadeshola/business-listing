package com.business.resources;

import com.business.jpa.entity.Business;
import com.business.jpa.entity.Category;
import com.business.model.request.BusinessRequest;
import com.business.model.response.BusinessResponse;
import com.business.model.response.PaginateResponse;
import com.business.services.BusinessService;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/business")
@Slf4j
@Api(value = "api/v1/business", description = "Endpoint for business management", tags = "Business Management")
public class BusinessControlller {

    private BusinessService businessService;
    private ModelMapper modelMapper;

    public BusinessControlller(BusinessService businessService, ModelMapper modelMapper) {
        this.businessService = businessService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ApiOperation(httpMethod = "POST", value = "Resource to create a business", response = BusinessResponse.class, nickname = "createBusiness")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! Business created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different business name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<BusinessResponse> createBusiness(@Valid @RequestBody BusinessRequest request) {
        Business business = businessService.createBusiness(request);
        BusinessResponse response = modelMapper.map(business, BusinessResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER' )")
    @PutMapping("/{businessId}")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a business", response = BusinessResponse.class, nickname = "updateBusiness")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Business updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Business ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<BusinessResponse> updateBusiness(
            @Valid @RequestBody BusinessRequest request,
            @ApiParam(name = "businessId", value = "Provide Business ID", required = true) @PathVariable("businessId") Long businessId) {
        Business business = businessService.updateBusiness(request, businessId);
        BusinessResponse response = modelMapper.map(business, BusinessResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{name}")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a business by business name", response = BusinessResponse.class, nickname = "findBusinessByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Business"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<BusinessResponse> findBusinessByName(
            @ApiParam(name = "name", value = "Provide Business Name", required = true)
            @PathVariable(value = "name") String name) {
        Optional<Business> optionalBusiness = businessService.findBusinessByName(name);
        if(optionalBusiness.isPresent()) {
            Business business = optionalBusiness.get();
            BusinessResponse response = modelMapper.map(business, BusinessResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{businessId}")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a business", responseReference = "true", nickname = "deleteBusiness")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Business deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Business ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteBusiness(
            @ApiParam(name = "businessId", value = "Provide Business ID", required = true) @PathVariable(value = "businessId") Long businessId) {
        businessService.deleteBusinessById(businessId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @ApiOperation(httpMethod = "GET", value = "Resource to view all business", response = PaginateResponse.class, nickname = "findAllCountries")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View All Countries"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<PaginateResponse> findAllCountries(
            @ApiParam(name = "page", value = "default number of page", required = true) @RequestParam(value = "page", defaultValue = "0") int page,
            @ApiParam(name = "size", value = "default size on result set", required = true) @RequestParam(value = "size", defaultValue = "5") int size ) {

        //Applying pagination and sorting date create in descending order
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateCreated");

        Page<Business> businesses = businessService.findAllBusiness(pageable);

        List<BusinessResponse> businessResponses = new ArrayList<>();
        businesses.forEach(business -> {
            BusinessResponse response = modelMapper.map(business, BusinessResponse.class);
            businessResponses.add(response);
        });
        PaginateResponse response = new PaginateResponse();
        response.setContents(businessResponses);
        response.setTotalElements(businesses.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
