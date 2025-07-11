package com.nusiss.userservice.dao;

import com.nusiss.userservice.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
}
