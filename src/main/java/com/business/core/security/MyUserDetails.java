package com.business.core.security;

import com.business.core.exceptions.CustomException;
import com.business.core.utils.AppUtils;
import com.business.jpa.entity.User;
import com.business.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Slf4j
public class MyUserDetails implements UserDetailsService {

    private UserRepository userRepository;
    private AuthorityDetails authorityDetails;

    @Autowired
    public MyUserDetails(UserRepository userRepository, AuthorityDetails authorityDetails) {
        this.userRepository = userRepository;
        this.authorityDetails = authorityDetails;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            Optional<User> userOptional = userRepository.findUserByUsername(username);

            log.info("user object : {}", AppUtils.toJSON(userOptional));

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getStatus().equals(true)) {
                    user.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
                    userRepository.saveAndFlush(user);
                }else {
                    log.error("Error occurred :: User status is: {}", user.getStatus());
                    throw new CustomException("User is not active, please contact your administrator", HttpStatus.LOCKED);
                }

                    return org.springframework.security.core.userdetails.User//
                            .withUsername(username)//
                            .password(user.getPassword())//
                            .authorities(authorityDetails.getAuthorities(user))//
                            .accountExpired(false)//
                            .accountLocked(false)//
                            .credentialsExpired(false)//
                            .disabled(false)//
                            .build();
                }
        } catch(Exception e){
            log.error("Error occurred :: {}", e.getMessage());
            throw new CustomException("user not found", e.getCause(), e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return null;
    }
}
