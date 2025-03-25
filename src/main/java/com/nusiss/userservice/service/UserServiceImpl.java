package com.nusiss.userservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.userservice.config.CustomException;
import com.nusiss.userservice.dao.AddressRepository;
import com.nusiss.userservice.dao.PermissionRepository;
import com.nusiss.userservice.dao.UserRepository;
import com.nusiss.userservice.entity.Address;
import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RedisCrudService redisCrudService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PermissionRepository permissionRepository;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id).map(user -> {
            // Encrypt the password using bcrypt
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            // Replace the user's password with the encrypted version
            user.setPassword(encryptedPassword);
            return user;
        });

    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findUserByUsernameAndPassword(String username, String password){

        return userRepository.findUserByUsernameAndPassword(username, password);
    }

    @Override
    public User getCurrentUserInfo(String authToken) {
        try{
            Claims claims =  Jwts.parserBuilder()
                    .setSigningKey(JwtTokenService.SECRET_KEY) // Set the key used to validate the signature
                    .build()
                    .parseClaimsJws(authToken) // Parse the token
                    .getBody(); // Get the claims from the token


            // Extract information from the claims
            String username = claims.get("username", String.class);
            if(redisCrudService.exists(username)){
                String userJson = redisCrudService.get(username);
                return objectMapper.readValue(userJson, User.class);
            } else {
                throw new CustomException("Fail to get user info: user didn't login");
            }

        } catch (Exception e){
            logger.info("Fail to get user info: {}", e.getMessage());

            throw new CustomException("Fail to get user info: {}" + e.getMessage());
        }

    }

    @Override
    public boolean hasPermission(String authToken, String requestedApi, String method) {

        User user = getCurrentUserInfo(authToken);
        if (user == null) {
            return false;
        }

        // Get all permissions associated with the user's roles
        Set<Permission> permissions = permissionRepository.findPermissionsByUserRoles(user.getUserId());

        // Check if the user has permission for the requested API and method
        return permissions.stream()
                .anyMatch(permission -> permission.getEndpoint().equals(requestedApi) && permission.getMethod().equals(method));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }


//    @Override
//    public User saveUser(User user, List<Address> addresses) {
//        // 保存用户
//        User savedUser = userRepository.save(user);
//
//        // 保存用户的地址信息
//        if (addresses != null && !addresses.isEmpty()) {
//            addresses.forEach(address -> {
//                address.setUser(savedUser);
//                addressRepository.save(address);
//            });
//        }
//
//        return savedUser;
//    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(Integer.valueOf(id));
    }
}
