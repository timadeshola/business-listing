package com.business.core.security;

import com.business.core.exceptions.CustomException;
import com.business.jpa.entity.Role;
import com.business.jpa.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class AuthorityDetails {

    public Set<GrantedAuthority> getAuthorities (User user){
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            if (role.getStatus().equals(false)) {
                throw new CustomException("Role has been disabled", HttpStatus.LOCKED);
            }
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
        }
        return authorities;
    }
}
