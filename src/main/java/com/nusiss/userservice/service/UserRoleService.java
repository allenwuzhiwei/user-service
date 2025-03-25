package com.nusiss.userservice.service;

import com.nusiss.userservice.entity.Role;

import java.util.Optional;

public interface UserRoleService
{
    public Optional<Role> getRoleByUserId(Long userId);

    public void createRoleForUser(Long roleId, Long userId);
}
