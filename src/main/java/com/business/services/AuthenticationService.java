package com.business.services;

import com.business.core.security.JwtAuthenticationRequest;
import com.business.core.security.UserTokenState;

public interface AuthenticationService {

    UserTokenState createAuthenticationToken(JwtAuthenticationRequest authenticationRequest);
}
