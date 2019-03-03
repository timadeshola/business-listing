package com.business.resources;

import com.business.jpa.entity.Address;
import com.business.model.request.AddressRequest;
import com.business.model.response.AddressResponse;
import com.business.model.response.PaginateResponse;
import com.business.services.AddressService;
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
@RequestMapping("api/v1/address")
@Slf4j
@Api(value = "api/v1/address", description = "Endpoint for address management", tags = "Address Management")
public class AddressControlller {

    private AddressService addressService;
    private ModelMapper modelMapper;

    public AddressControlller(AddressService addressService, ModelMapper modelMapper) {
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ApiOperation(httpMethod = "POST", value = "Resource to create a address", response = AddressResponse.class, nickname = "createAddress")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! Address created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different address name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody AddressRequest request) {
        Address address = addressService.createAddress(request);
        AddressResponse response = modelMapper.map(address, AddressResponse.class);
        response.setState(address.getState().getName());
        response.setCountry(address.getCountry().getName());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER' )")
    @PutMapping("/{addressId}")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a address", response = AddressResponse.class, nickname = "updateAddress")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Address updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Address ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<AddressResponse> updateAddress(
            @Valid @RequestBody AddressRequest request,
            @ApiParam(name = "addressId", value = "Provide Address ID", required = true) @PathVariable("addressId") Long addressId) {
        Address address = addressService.updateAddress(request, addressId);
        AddressResponse response = modelMapper.map(address, AddressResponse.class);
        response.setState(address.getState().getName());
        response.setCountry(address.getCountry().getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{houseNo}")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a address by address name", response = AddressResponse.class, nickname = "findAddressByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Address"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<AddressResponse> findAddressByName(
            @ApiParam(name = "houseNo", value = "Provide House No", required = true)
            @PathVariable(value = "houseNo") String houseNo) {
        Optional<Address> optionalRole = addressService.findAddressByName(houseNo);
        if (optionalRole.isPresent()) {
            AddressResponse response = modelMapper.map(optionalRole.get(), AddressResponse.class);
            response.setState(optionalRole.get().getState().getName());
            response.setCountry(optionalRole.get().getCountry().getName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{addressId}")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a address", responseReference = "true", nickname = "deleteAddress")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Address deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Address ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteAddress(
            @ApiParam(name = "addressId", value = "Provide Address ID", required = true)
            @PathVariable(value = "addressId") Long addressId) {
        addressService.deleteAddressById(addressId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @ApiOperation(httpMethod = "GET", value = "Resource to view all address", response = PaginateResponse.class, nickname = "findAllCountries")
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
        Page<Address> addresses = addressService.findAllAddress(pageable);
        List<AddressResponse> responses = new ArrayList<>();
        addresses.forEach(address -> {
            AddressResponse response = modelMapper.map(address, AddressResponse.class);
            response.setState(address.getState().getName());
            response.setCountry(address.getCountry().getName());
            responses.add(response);
        });
        PaginateResponse response = new PaginateResponse();
        response.setContents(responses);
        response.setTotalElements(addresses.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
