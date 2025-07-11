package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.RolePermission;
import com.nusiss.userservice.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {

    @Autowired
    RolePermissionService rolePermissionService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolePermission>> getPermissionById(@PathVariable Integer id) {
        Optional<RolePermission> p = rolePermissionService.get(id);
        return p.map(value -> ResponseEntity.ok(new ApiResponse<>(true, "RolePermission found", value)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "RolePermission not found", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<RolePermission>>> createPermission(@RequestBody List<RolePermission> dtos) {
        List<RolePermission> results = new ArrayList<>();
        if(dtos != null){
            for (RolePermission rolePermission : dtos){
                RolePermission created = rolePermissionService.create(rolePermission);
                results.add(created);
            }
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Permission created successfully", results));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable Integer id) {
        rolePermissionService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Permission deleted successfully", null));
    }
}
