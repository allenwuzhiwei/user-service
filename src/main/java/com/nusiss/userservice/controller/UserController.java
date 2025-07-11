package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.dto.UserWithRolesDTO;
import com.nusiss.userservice.entity.Address;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.entity.UserWithRolesProjection;
import com.nusiss.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserWithRolesProjection>>> searchUsers(@RequestParam(defaultValue = "") String username,
                                                               @RequestParam(defaultValue = "") String email,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "createDatetime") String sortBy,
                                                               @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        List<UserWithRolesProjection> users = userService.findUsers(username, email , pageable);

        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", value)))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "User created successfully", savedUser));
    }

//    @PostMapping
//    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user, @RequestBody List<Address> addresses) {
//        User savedUser = userService.saveUser(user, addresses);  // 修改：传递地址列表
//        return ResponseEntity.status(201).body(new ApiResponse<>(true, "User and address created successfully", savedUser));
//    }

    @PutMapping
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody User updatedUser) {
        try {
            User savedUser = userService.updateUser(updatedUser);
            return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", savedUser));

        }catch(Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "User not found", null));

        }

    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Integer id) {
//        Optional<User> existingUser = userService.getUserById(id);
//        if (existingUser.isPresent()) {
//            userService.deleteUser(id);
//            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
//        } else {
//            return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null));
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Integer id) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "User and address deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null));
        }
    }
}
