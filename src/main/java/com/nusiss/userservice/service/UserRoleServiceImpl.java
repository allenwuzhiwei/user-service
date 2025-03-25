package com.nusiss.userservice.service;

import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.entity.UserRole;
import com.nusiss.userservice.entity.UserRoleId;
import com.nusiss.userservice.dao.RoleRepository;
import com.nusiss.userservice.dao.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> getRoleByUserId(Long userId) {
        Optional<UserRole> userRole = userRoleRepository.findByIdUserId(userId);
        if (userRole.isPresent()) {
            return roleRepository.findById(userRole.get().getId().getRoleId());
        }
        return Optional.empty();
    }

    @Override
    public void createRoleForUser(Long roleId, Long userId) {
        // 创建 UserRoleId 作为复合主键
        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setUserId(userId);
        userRoleId.setRoleId(roleId);

        // 创建 UserRole 实体并保存
        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);

        userRoleRepository.save(userRole);
    }
}