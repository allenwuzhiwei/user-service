package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.PermissionRepository;
import com.nusiss.userservice.dto.PermissionDTO;
import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.entity.PermissionProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired PermissionRepository permissionRepository;

    public Permission create(Permission p) { return permissionRepository.save(p); }
    public Optional<Permission> get(Integer id) { return permissionRepository.findById(id); }
    public List<Permission> list() { return permissionRepository.findAll(); }
    public Permission update(Integer id, Permission data) {
        var p = get(id).orElseThrow();
        p.setEndpoint(data.getEndpoint());
        p.setMethod(data.getMethod());
        return permissionRepository.save(p);
    }
    public void delete(Integer id) { permissionRepository.deleteById(id); }

    public Page<Permission> getAllPermissions(Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }

    public Page<PermissionDTO> getAllPermissionsDTO(String endpoint, Pageable pageable) {
        Page<PermissionProjection> projections = permissionRepository.findAllPermissionsWithRoles(endpoint, pageable);

        return projections.map(p -> {
            PermissionDTO dto = new PermissionDTO();
            dto.setId(p.getId());
            dto.setEndpoint(p.getEndpoint());
            dto.setMethod(p.getMethod());
            dto.setCreateUser(p.getCreateUser());
            dto.setCreateDatetime(p.getCreateDatetime());
            dto.setUpdateUser(p.getUpdateUser());
            dto.setUpdateDatetime(p.getUpdateDatetime());
            if (p.getRoles() != null) {
                dto.setRoles(Arrays.asList(p.getRoles().split(",")));
            } else {
                dto.setRoles(Collections.emptyList());
            }
            return dto;
        });
    }

    private PermissionDTO toDTO(Permission p) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(p.getId());
        dto.setEndpoint(p.getEndpoint());
        dto.setMethod(p.getMethod());
        dto.setDescription(p.getDescription());
        dto.setCreateUser(p.getCreateUser());
        dto.setCreateDatetime(p.getCreateDatetime());
        dto.setUpdateUser(p.getUpdateUser());
        dto.setUpdateDatetime(p.getUpdateDatetime());
        return dto;
    }
}
