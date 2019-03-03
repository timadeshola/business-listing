package com.business.services.impl;

import com.business.core.exceptions.CustomException;
import com.business.core.security.JwtAuthenticationRequest;
import com.business.core.security.TokenHelper;
import com.business.core.security.UserTokenState;
import com.business.jpa.entity.User;
import com.business.jpa.repository.UserRepository;
import com.business.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.business.core.constants.AppConstant.Security.EXPIRE_IN;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private TokenHelper tokenHelper;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public AuthenticationServiceImpl(TokenHelper tokenHelper, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.tokenHelper = tokenHelper;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public UserTokenState createAuthenticationToken(JwtAuthenticationRequest authenticationRequest) {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Inject into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // token creation
        Optional<User> optionalUser = userRepository.findUserByUsername(authenticationRequest.getUsername());
        if (!optionalUser.isPresent()) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        String jwtToken = tokenHelper.createToken(authenticationRequest.getUsername(), user.getRoles());

        return new UserTokenState(jwtToken, EXPIRE_IN);
    }
}
