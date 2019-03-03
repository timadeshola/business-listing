package com.business.services;

import com.business.jpa.entity.User;
import com.business.model.request.UpdateUserRequest;
import com.business.model.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface UserService {

    User createUser(UserRequest request);
    User updateUser(UpdateUserRequest request);
    void deleteUser(Long userId);
    Optional<User> viewUserByUsername(String username);
    Page<User> findAllUsers(Pageable pageable);
    Boolean toggleUserStatus(Long userId);
}
