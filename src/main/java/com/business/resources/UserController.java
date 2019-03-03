package com.business.resources;

import com.business.jpa.entity.User;
import com.business.model.request.UpdateUserRequest;
import com.business.model.request.UserRequest;
import com.business.model.response.PaginateResponse;
import com.business.model.response.UserResponse;
import com.business.services.UserService;
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
@RequestMapping("/api/v1/users")
@Slf4j
@Api(value = "api/v1/users", description = "Endpoint for user management", tags = "User Management")
public class UserController {

    private UserService userService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ApiOperation(httpMethod = "POST", value = "Resource to create a user", response = UserResponse.class, nickname = "createUser")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! User created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation Failed"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different user name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        User user = userService.createUser(request);
        UserResponse response = modelMapper.map(user, UserResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a user", responseReference = "true", nickname = "updateUser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! User updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the User ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        userService.updateUser(request);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a user", responseReference = "true", nickname = "deleteUser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! User deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Resource not found for the User ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteUser(
            @ApiParam(name = "userId", value = "Provide User ID", required = true)
            @RequestParam(value = "userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    @ApiOperation(httpMethod = "GET", value = "Resource to view all users", response = PaginateResponse.class, nickname = "findAllUsers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View All Users"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<PaginateResponse> findAllUsers(@ApiParam(name = "page", value = "default number of page", required = true)
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @ApiParam(name = "size", value = "default size on result set", required = true)
                                                         @RequestParam(value = "size", defaultValue = "5") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateCreated");
        Page<User> users = userService.findAllUsers(pageable);

        List<UserResponse> responses = new ArrayList<>();
        users.getContent().forEach(user -> {
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            responses.add(userResponse);
        });

        PaginateResponse response = new PaginateResponse();
        response.setContents(responses);
        response.setTotalElements(users.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a user by User name", response = UserResponse.class, nickname = "viewUserByUsername")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a User by Username"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<UserResponse> viewUserByUsername(
            @PathVariable(value = "username") String username) {
        Optional<User> optionalUser = userService.viewUserByUsername(username);
        if(optionalUser.isPresent()) {
            UserResponse response = modelMapper.map(optionalUser.get(), UserResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(httpMethod = "PUT", value = "Resource to toggle user status", responseReference = "true", nickname = "toggleUserStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Toggle user status successful"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> toggleUserStatus(
            @ApiParam(name = "userId", value = "Provide User ID", required = true)
            @PathVariable(value = "userId") Long userId) {
        userService.toggleUserStatus(userId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
