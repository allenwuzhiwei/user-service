package com.nusiss.userservice.dao;


import com.nusiss.userservice.entity.User;
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
    SELECT new com.nusiss.userservice.entity.User(
        u.userId, u.username, u.email,
        u.createDatetime, u.updateDatetime,
        u.createUser, u.updateUser)
    FROM User u
    WHERE (:username IS NULL OR :username = '' OR u.username = :username)
      AND (:email IS NULL OR :email = '' OR u.email = :email)
""")
    List<User> searchUsers(@Param("username") String username, @Param("email") String email, Pageable pageable);
}