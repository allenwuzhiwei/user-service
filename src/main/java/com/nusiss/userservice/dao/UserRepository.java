package com.nusiss.userservice.dao;


import com.nusiss.userservice.dto.UserWithRolesDTO;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.entity.UserWithRolesProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    @Query(
            value = "SELECT * FROM user u WHERE u.username = ?1 and u.password = ?2",
            nativeQuery = true)
    List<User> findUserByUsernameAndPassword(String username, String password);

    @Query("""
    SELECT u.userId AS userId, u.username AS username, u.email AS email,
       u.createDatetime AS createDatetime, u.updateDatetime AS updateDatetime,
       u.createUser AS createUser, u.updateUser AS updateUser,
       r2.name AS roleName
    FROM User u
    LEFT JOIN UserRole ur ON ur.userId = u.userId
    LEFT JOIN Role r2 ON r2.id = ur.roleId
    WHERE (:username IS NULL OR :username = '' OR u.username LIKE CONCAT('%', LOWER(:username), '%'))
      AND (:email IS NULL OR :email = '' OR u.email LIKE CONCAT('%', LOWER(:email), '%'))
    """)
    List<UserWithRolesProjection> searchUsersWithRoles(
            @Param("username") String username,
            @Param("email") String email,
            Pageable pageable
    );
}