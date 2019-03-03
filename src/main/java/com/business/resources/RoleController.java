package com.business.resources;

import com.business.jpa.entity.Role;
import com.business.model.request.RoleRequest;
import com.business.model.request.UpdateRoleRequest;
import com.business.model.response.PaginateResponse;
import com.business.model.response.RoleResponse;
import com.business.services.RoleService;
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

/**
 * This class manages the application role's endpoint
 * It is comprise of the below endpoints:
 * Create Role Endpoint
 * Update Role Endpoint
 * Delete Role Endpoint
 * View Role By Role Name Endpoint
 * Toggle Role Status to either true or false
 */

@RestController
@RequestMapping("api/v1/roles")
@Slf4j
@Api(value = "api/v1/roles", description = "Endpoint for role management", tags = "Role Management")
public class RoleController {

    private RoleService roleService;
    private ModelMapper modelMapper;

    public RoleController(RoleService roleService, ModelMapper modelMapper) {
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ApiOperation(httpMethod = "POST", value = "Resource to create a role", response = RoleResponse.class, nickname = "createRole")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! Role created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different role name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        Role role = roleService.createRole(request);
        RoleResponse response = modelMapper.map(role, RoleResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a role", response = RoleResponse.class, nickname = "updateRole")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Role updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Role ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody UpdateRoleRequest request) {
        Role role = roleService.updateRole(request);
        RoleResponse response = modelMapper.map(role, RoleResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a role", responseReference = "true", nickname = "deleteRole")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Role deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Role ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteRole(
            @ApiParam(name = "roleId", value = "Provide Role ID", required = true)
            @RequestParam(value = "roleId") Long roleId) {
        roleService.deleteRole(roleId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    @ApiOperation(httpMethod = "GET", value = "Resource to view all roles", response = PaginateResponse.class, nickname = "viewAllRoles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View All Roles"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<PaginateResponse> viewAllRoles(@ApiParam(name = "page", value = "default number of page", required = true)
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @ApiParam(name = "size", value = "default size on result set", required = true)
                                                   @RequestParam(value = "size", defaultValue = "5") int size ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateCreated");
        Page<Role> roles = roleService.findAllRoles(pageable);

        PaginateResponse response = new PaginateResponse();
        response.setContents(roles.getContent());
        response.setTotalElements(roles.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{name}")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a role by Role Name", response = RoleResponse.class, nickname = "viewRoleByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Role"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<RoleResponse> viewRoleByName(
            @ApiParam(name = "name", value = "Provide Role Name", required = true)
            @PathVariable(value = "name") String name) {
        Optional<Role> optionalRole = roleService.viewRoleByName(name);
        if(optionalRole.isPresent()) {
            RoleResponse response = modelMapper.map(optionalRole.get(), RoleResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{roleId}")
    @ApiOperation(httpMethod = "PUT", value = "Resource to toggle role status", responseReference = "true", nickname = "toggleRoleStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Toggle role status successful"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> toggleRoleStatus(@ApiParam(name = "roleId", value = "Provide Role ID", required = true)
            @PathVariable(value = "roleId") Long roleId) {
        roleService.toggleRoleStatus(roleId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
