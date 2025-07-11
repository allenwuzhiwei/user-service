package com.nusiss.userservice.dao;

import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.entity.PermissionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;


public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query(value = "SELECT p.* FROM permissions p JOIN role_permissions rp ON rp.permission_id = p.id " +
            "            JOIN user_roles ur ON ur.role_id= rp.role_id WHERE ur.user_id = ?1", nativeQuery = true)
    Set<Permission> findPermissionsByUserRoles(Integer userId);

    Page<Permission> findByEndpointContainingIgnoreCase(String endpoint, Pageable pageable);

    @Query(
            value = """
        SELECT 
          p.id AS id,
          p.endpoint AS endpoint,
          p.method AS method,
          p.create_user AS createUser,
          p.create_datetime AS createDatetime,
          p.update_user AS updateUser,
          p.update_datetime AS updateDatetime,
          GROUP_CONCAT(r.name) AS roles
        FROM 
          permissions p
        LEFT JOIN 
          role_permissions rp ON p.id = rp.permission_id
        LEFT JOIN 
          roles r ON rp.role_id = r.id
        WHERE 
          (:endpoint IS NULL OR p.endpoint LIKE %:endpoint%)
        GROUP BY p.id
        """,
            countQuery = """
        SELECT COUNT(DISTINCT p.id) 
        FROM permissions p 
        WHERE (:endpoint IS NULL OR p.endpoint LIKE %:endpoint%)
        """,
            nativeQuery = true
    )
    Page<PermissionProjection> findAllPermissionsWithRoles(
            @Param("endpoint") String endpoint,
            Pageable pageable
    );

    boolean existsByEndpointAndMethod(String endpoint, String method);
}
