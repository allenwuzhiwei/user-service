package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.RoleRepository;
import com.nusiss.userservice.dao.UserRoleRepository;
import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    public Optional<Role> getRoleByUserId(Integer userId) {
        Optional<UserRole> userRole = userRoleRepository.findByUserId(userId);
        if (userRole.isPresent()) {
            return roleRepository.findById(userId);
        }
        return Optional.empty();
    }

    public void createRoleForUser(Integer roleId, Integer userId) {
        // 创建 UserRoleId 作为复合主键

        // 创建 UserRole 实体并保存
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);

        userRoleRepository.save(userRole);
    }


    public UserRole create(UserRole ur) {
        //delete old relationship first,one user can only have on record
        userRoleRepository.deleteByUserId(ur.getUserId());
        return userRoleRepository.save(ur);
    }
    public Optional<UserRole> get(Integer id) { return userRoleRepository.findById(id); }
    public List<UserRole> list() { return userRoleRepository.findAll(); }
    public void delete(Integer id) { userRoleRepository.deleteById(id); }

    public void deleteByUserId(Integer userId){
        userRoleRepository.deleteByUserId(userId);
    }
}
