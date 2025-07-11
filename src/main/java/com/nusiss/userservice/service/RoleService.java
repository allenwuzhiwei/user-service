package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.RolePermissionRepository;
import com.nusiss.userservice.dao.RoleRepository;
import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.entity.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Autowired
    UserService userService;

    public Role create(Role r) {
        return roleRepository.save(r);
    }

    public Optional<Role> get(Integer id) { return roleRepository.findById(id); }
    public List<Role> list() { return roleRepository.findAll(); }
    public Role update(Integer id, Role data) {
        var r = get(id).orElseThrow();
        r.setName(data.getName());
        return roleRepository.save(r);
    }
    public void delete(Integer id) { roleRepository.deleteById(id); }

    public RolePermission assignPermission(Integer roleId, Integer permissionId) {
        RolePermission rp = new RolePermission();
        rp.setRoleId(roleId);
        rp.setPermissionId(permissionId);
        return rolePermissionRepository.save(rp);
    }
}
