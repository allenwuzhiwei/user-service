package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.UserRole;
import com.nusiss.userservice.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /*// 获取用户的角色（单个）
    @GetMapping("/user/{userId}")
    public ResponseEntity<Role> getUserRole(@PathVariable Integer userId) {
        Optional<Role> role = userRoleService.getRoleByUserId(userId);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 为用户创建角色
    @PostMapping("/create")
    public ResponseEntity<Role> createUserRole(@RequestParam Integer roleId, @RequestParam Integer userId) {
        userRoleService.createRoleForUser(roleId, userId);
        return ResponseEntity.ok().build();
    }*/

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserRole>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<UserRole> pageData = userRoleService.list();

        return ResponseEntity.ok(new ApiResponse<>(true, "UserRoles retrieved successfully", pageData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRole>> getById(@PathVariable Integer id) {
        Optional<UserRole> ur = userRoleService.get(id);
        return ur.map(value -> ResponseEntity.ok(new ApiResponse<>(true, "UserRole found", value)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Not found", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<UserRole>>> create(@RequestBody List<UserRole> urs) {
        List<UserRole> results = new ArrayList<>();
        if(urs != null && urs.size() > 0){
            for(UserRole userRole : urs){
                UserRole created = userRoleService.create(userRole);
                results.add(created);
            }
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "UserRole created successfully", results));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        userRoleService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "UserRole deleted successfully", null));
    }

    /*@DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteByUserId(@PathVariable Integer userId) {
        userRoleService.deleteByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "UserRole deleted successfully", null));
    }*/
}