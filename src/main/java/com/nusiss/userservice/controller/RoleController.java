package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Role>>> searchRole() {

        List<Role> listData = roleService.list();

        ApiResponse<List<Role>> response = new ApiResponse<>(true, "Roles retrieved successfully", listData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Integer id) {
        Optional<Role> role = roleService.get(id);
        return role.map(value -> ResponseEntity.ok(new ApiResponse<>(true, "Role found", value)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Role not found", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Role>> createRole(@RequestBody Role dto) {
        Role created = roleService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Role created successfully", created));
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<ApiResponse<Integer>> getRoleByUserId(@PathVariable Integer userId){
        Integer roleId = roleService.getRoleByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Role got successfully", roleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> updateRole(@PathVariable Integer id, @RequestBody Role dto) {
        try {
            Role updated = roleService.update(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Role updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Role deleted successfully", null));
    }
}
