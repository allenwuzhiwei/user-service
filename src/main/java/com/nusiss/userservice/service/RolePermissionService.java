package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.RolePermissionRepository;
import com.nusiss.userservice.entity.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolePermissionService {

    @Autowired
    RolePermissionRepository repo;

    public RolePermission create(RolePermission rp) {
        return repo.save(rp);
    }

    public Optional<RolePermission> get(Integer id) { return repo.findById(id); }
    public List<RolePermission> list() { return repo.findAll(); }
    public void delete(Integer id) { repo.deleteById(id); }
}
