package com.nusiss.userservice.service;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.dao.RolePermissionRepository;
import com.nusiss.userservice.entity.RolePermission;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RolePermissionService {

    @Autowired
    RolePermissionRepository repo;

    public RolePermission create(RolePermission rp) {
        return repo.save(rp);
    }

    public List<RolePermission> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId){

        return repo.findByRoleIdAndPermissionId(roleId, permissionId);
    }
    public Optional<RolePermission> get(Integer id) { return repo.findById(id); }
    public List<RolePermission> list() { return repo.findAll(); }
    public void delete(Integer id) { repo.deleteByPermissionId(id); }

    @Transactional
    public List<RolePermission> createPermission(List<RolePermission> dtos){
        List<RolePermission> results = new ArrayList<>();
        if(dtos != null){
            for (RolePermission rolePermission : dtos){

                //check if it exists
                List<RolePermission> pr = findByRoleIdAndPermissionId(rolePermission.getRoleId(), rolePermission.getPermissionId());
                if(pr == null || pr.size() == 0){
                    RolePermission created = create(rolePermission);
                    results.add(created);
                }

            }
        }

        return results;
    }
}
