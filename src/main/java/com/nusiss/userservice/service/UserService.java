package com.nusiss.userservice.service;

import com.nusiss.userservice.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> getAllUsers();

    public Optional<User> getUserById(Integer id);

    public User saveUser(User user);

    //User saveUser(User user, List<Address> addresses);

    public void deleteUser(Integer id);

    public List<User> findUserByUsernameAndPassword(String username, String password);

    public User getCurrentUserInfo(String authToken);

    public boolean hasPermission(String authToken, String requestedApi, String method);

    User findByUsername(String username);

    public List<User> findUsers(String username, String email, Pageable pageable);

    public User updateUser(User user);

}
