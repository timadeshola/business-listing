package com.business.resources;

import com.business.jpa.entity.States;
import com.business.model.request.StateRequest;
import com.business.model.response.PaginateResponse;
import com.business.model.response.StateResponse;
import com.business.services.StateService;
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
@RequestMapping("api/v1/states")
@Slf4j
@Api(value = "api/v1/states", description = "Endpoint for state management", tags = "State Management")
public class StateControlller {

    private StateService stateService;
    private ModelMapper modelMapper;

    public StateControlller(StateService stateService, ModelMapper modelMapper) {
        this.stateService = stateService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ApiOperation(httpMethod = "POST", value = "Resource to create a state", response = StateResponse.class, nickname = "createState")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! State created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different state name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<StateResponse> createState(@Valid @RequestBody StateRequest request) {
        States state = stateService.createState(request);
        StateResponse response = modelMapper.map(state, StateResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{stateId}")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a state", response = StateResponse.class, nickname = "updateState")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! State updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the State ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<StateResponse> updateState(
            @Valid @RequestBody StateRequest request,
            @ApiParam(name = "stateId", value = "Provide State ID", required = true) @PathVariable("stateId") Long stateId) {
        States state = stateService.updateState(request, stateId);
        StateResponse response = modelMapper.map(state, StateResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{name}")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a state by state name", response = StateResponse.class, nickname = "findStateByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a State"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<StateResponse> findStateByName(
            @ApiParam(name = "name", value = "Provide State Name", required = true)
            @PathVariable(value = "name") String name) {
        Optional<States> optionalRole = stateService.findStateByName(name);
        if (optionalRole.isPresent()) {
            StateResponse response = modelMapper.map(optionalRole.get(), StateResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{stateId}")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a state", responseReference = "true", nickname = "deleteState")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! State deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the State ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteState(
            @ApiParam(name = "stateId", value = "Provide State ID", required = true) @PathVariable(value = "stateId") Long stateId) {
        stateService.deleteStateById(stateId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @ApiOperation(httpMethod = "GET", value = "Resource to view all state", response = PaginateResponse.class, nickname = "findAllCountries")
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
        Page<States> states = stateService.findAllStates(pageable);
        List<StateResponse> stateResponses = new ArrayList<>();
        states.forEach(state -> {
            StateResponse stateResponse = modelMapper.map(state, StateResponse.class);
            stateResponses.add(stateResponse);
        });
        PaginateResponse response = new PaginateResponse();
        response.setContents(stateResponses);
        response.setTotalElements(states.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
