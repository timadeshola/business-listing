package com.business.resources;

import com.business.core.security.JwtAuthenticationRequest;
import com.business.core.security.UserTokenState;
import com.business.services.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Api(value = "api/v1/auth", description = "Endpoint for authentication management", tags = "Authentication Management")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    @ApiOperation(httpMethod = "POST", value = "Resource to login into the system", response = UserTokenState.class, responseReference = "Authentication token", nickname = "createAuthenticationToken")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! User authenticated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        UserTokenState authenticationToken = authenticationService.createAuthenticationToken(authenticationRequest);
        log.info("#########TOKEN::: {}", authenticationToken.getAccess_token());
        return new ResponseEntity<>(authenticationToken, HttpStatus.OK);
    }
}
