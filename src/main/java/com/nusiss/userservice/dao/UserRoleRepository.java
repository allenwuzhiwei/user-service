package com.nusiss.userservice.dao;

import com.nusiss.userservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.userId = :userId")
    Optional<UserRole> findByUserId(@Param("userId") Integer userId);
}