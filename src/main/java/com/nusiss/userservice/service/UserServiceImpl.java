package com.nusiss.userservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.userservice.config.CustomException;
import com.nusiss.userservice.dao.AddressRepository;
import com.nusiss.userservice.dao.PermissionRepository;
import com.nusiss.userservice.dao.UserRepository;
import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id).map(user -> {
            // Encrypt the password using bcrypt
            //String encryptedPassword = passwordEncoder.encode(user.getPassword());
            // Replace the user's password with the encrypted version
            //user.setPassword("");
            return user;
        });

    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findUsers(String username, String email, Pageable pageable) {
        List<User> user = userRepository.searchUsers(username, email, pageable);

        return user;
    }

    @Override
    public User updateUser(User user) {
        // Validate ID presence
        if (user.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null for update.");
        }

        // Fetch existing user
        User existing = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getUserId()));

        // Check fields and update only if not blank/null
        if (StringUtils.isNotBlank(user.getUsername())) {
            existing.setUsername(user.getUsername());
        }

        if (StringUtils.isNotBlank(user.getEmail())) {
            existing.setEmail(user.getEmail());
        }

        if (StringUtils.isNotBlank(user.getPassword())) {
            //password from frontend is not same with password in DB
            if(!passwordEncoder.matches(existing.getPassword(), user.getPassword())){
                existing.setPassword(passwordEncoder.encode(user.getPassword()));
            }

        }

        if (StringUtils.isNotBlank(user.getUpdateUser())) {
            existing.setUpdateUser(user.getUpdateUser());
        }

        existing.setUpdateDatetime(LocalDateTime.now());

        return userRepository.save(existing);
    }

    @Override
    public List<User> findUserByUsernameAndPassword(String username, String password){

        return userRepository.findUserByUsernameAndPassword(username, password);
    }

    @Override
    public User getCurrentUserInfo(String authToken) {
        if (StringUtils.isNotBlank(authToken) && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        try{
            String username = Jwts.parserBuilder()
                    .setSigningKey(JwtTokenService.key)
                    .build()
                    .parseClaimsJws(authToken)
                    .getBody()
                    .getSubject();

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
        //only save no user
        if (!StringUtils.isEmpty(user.getPassword())) {
            if(user.getUserId() == null){
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            //for update, do nothing
        }
        user.setCreateUser("System");
        user.setUpdateUser("System");
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
