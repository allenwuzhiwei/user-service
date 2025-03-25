package com.nusiss.userservice.controller;

import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    // 获取用户的角色（单个）
    @GetMapping("/user/{userId}")
    public ResponseEntity<Role> getUserRole(@PathVariable Long userId) {
        Optional<Role> role = userRoleService.getRoleByUserId(userId);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 为用户创建角色
    @PostMapping("/create")
    public ResponseEntity<Role> createUserRole(@RequestParam Long roleId, @RequestParam Long userId) {
        userRoleService.createRoleForUser(roleId, userId);
        return ResponseEntity.ok().build();
    }

}