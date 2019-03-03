package com.business.resources;

import com.business.jpa.entity.Country;
import com.business.model.request.CountryRequest;
import com.business.model.response.PaginateResponse;
import com.business.model.response.CountryResponse;
import com.business.services.CountryService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/countries")
@Slf4j
@Api(value = "api/v1/countries", description = "Endpoint for country management", tags = "Country Management")
public class CountryControlller {

    private CountryService countryService;
    private ModelMapper modelMapper;

    public CountryControlller(CountryService countryService, ModelMapper modelMapper) {
        this.countryService = countryService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ApiOperation(httpMethod = "POST", value = "Resource to create a country", response = CountryResponse.class, nickname = "createCountry")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! Country created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different country name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<CountryResponse> createCountry(@Valid @RequestBody CountryRequest request) {
        Country country = countryService.createCountry(request);
        CountryResponse response = modelMapper.map(country, CountryResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER' )")
    @PutMapping("/{countryId}")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a country", response = CountryResponse.class, nickname = "updateCountry")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Country updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Country ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<CountryResponse> updateCountry(
            @Valid @RequestBody CountryRequest request,
            @ApiParam(name = "countryId", value = "Provide Country ID", required = true) @PathVariable("countryId") Long countryId) {
        Country country = countryService.updateCountry(request, countryId);
        CountryResponse response = modelMapper.map(country, CountryResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{name}")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a country by country name", response = CountryResponse.class, nickname = "findCountryByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Country"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<CountryResponse> findCountryByName(
            @ApiParam(name = "name", value = "Provide Country Name", required = true)
            @PathVariable(value = "name") String name) {
        Optional<Country> optionalRole = countryService.findCountryByName(name);
        if (optionalRole.isPresent()) {
            CountryResponse response = modelMapper.map(optionalRole.get(), CountryResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{countryId}")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a country", responseReference = "true", nickname = "deleteCountry")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Country deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Country ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteCountry(
            @ApiParam(name = "countryId", value = "Provide Country ID", required = true)
            @PathVariable(value = "countryId") Long countryId) {
        countryService.deleteCountryById(countryId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @ApiOperation(httpMethod = "GET", value = "Resource to view all country", response = PaginateResponse.class, nickname = "findAllCountries")
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
        Page<Country> countries = countryService.findAllCountries(pageable);
        List<CountryResponse> countryResponses = new ArrayList<>();
        countries.forEach(country -> {
            CountryResponse countryResponse = modelMapper.map(country, CountryResponse.class);
            countryResponses.add(countryResponse);
        });
        PaginateResponse response = new PaginateResponse();
        response.setContents(countryResponses);
        response.setTotalElements(countries.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
