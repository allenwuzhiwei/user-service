package com.nusiss.userservice.dao;

import com.nusiss.userservice.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    Optional<RolePermission> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);

    void deleteByPermissionId(Integer permissionId);
}
