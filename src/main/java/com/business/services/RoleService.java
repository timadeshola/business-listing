package com.business.services;

import com.business.jpa.entity.Role;
import com.business.model.request.RoleRequest;
import com.business.model.request.UpdateRoleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoleService {

    Role createRole(RoleRequest request);

    Role updateRole(UpdateRoleRequest request);

    void deleteRole(Long roleId);

    Optional<Role> viewRoleByName(String name);

    Page<Role> findAllRoles(Pageable pageable);

    Boolean toggleRoleStatus(Long roleId);
}
