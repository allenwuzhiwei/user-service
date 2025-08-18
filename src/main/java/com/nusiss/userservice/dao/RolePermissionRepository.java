package com.nusiss.userservice.dao;

import com.nusiss.userservice.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    List<RolePermission> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM role_permissions WHERE permission_id = :permissionId", nativeQuery = true)
    void deleteByPermissionId(@Param("permissionId") Integer permissionId);
}
