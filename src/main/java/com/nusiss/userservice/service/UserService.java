package com.nusiss.userservice.service;

import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.entity.Address;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> getAllUsers();

    public Optional<User> getUserById(Integer id);

    public Optional<User> getUserByUsername(String username);

    public User saveUser(User user);

    //User saveUser(User user, List<Address> addresses);

    public void deleteUser(Integer id);

    public List<User> findUserByUsernameAndPassword(String username, String password);

    public User getCurrentUserInfo(String authToken);

    public boolean hasPermission(String authToken, String requestedApi, String method);

}
