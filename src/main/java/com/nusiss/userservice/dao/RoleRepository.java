package com.nusiss.userservice.dao;

import com.nusiss.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select r.id FROM user_roles ur left join roles r on ur.role_id = r.id WHERE ur.user_id = :userId", nativeQuery = true)
    Integer getRoleByUserId(@Param("userId") Integer userId);

}