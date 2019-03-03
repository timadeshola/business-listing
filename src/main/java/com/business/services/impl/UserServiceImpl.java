package com.business.services.impl;

import com.business.core.exceptions.CustomException;
import com.business.jpa.entity.Role;
import com.business.jpa.entity.User;
import com.business.jpa.repository.RoleRepository;
import com.business.jpa.repository.UserRepository;
import com.business.model.request.UpdateUserRequest;
import com.business.model.request.UserRequest;
import com.business.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User createUser(UserRequest resource) {
        Optional<User> optionalUser = userRepository.findUserByUsername(resource.getUsername());
        if(optionalUser.isPresent()) {
            throw new CustomException("User already exist with the given title, please choose another username", HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setUsername(resource.getUsername());
        user.setPassword(passwordEncoder.encode(resource.getPassword()));
        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setEmail(resource.getEmail());

        if(resource.getRoleIds() != null) {
            List<Role> roleList = roleRepository.findAllById(resource.getRoleIds());
            Set<Role> roles = new HashSet<>(roleList);
            user.setRoles(roles);
        }
        user.setStatus(true);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UpdateUserRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getId());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(request.getFirstName() != null) {
                user.setFirstName(request.getFirstName());
            }
            if(request.getLastName() != null) {
                user.setLastName(request.getLastName());
            }
            if(request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }

            if(request.getRoleIds() != null) {
                List<Role> roleList = roleRepository.findAllById(request.getRoleIds());
                Set<Role> roles = new HashSet<>(roleList);
                user.setRoles(roles);
            }
            return userRepository.saveAndFlush(user);
        }
        throw new CustomException("User does not exist", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> viewUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Boolean toggleUserStatus(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getStatus().equals(true)) {
                user.setStatus(false);
            }else {
                user.setStatus(true);
            }
            userRepository.saveAndFlush(user);
            return true;
        }
        return false;
    }
}
