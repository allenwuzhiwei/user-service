package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.RolePermissionRepository;
import com.nusiss.userservice.dao.RoleRepository;
import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.entity.RolePermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoleByUserId() {
        Integer userId = 1;
        Integer roleId = 5;

        when(roleRepository.getRoleByUserId(userId)).thenReturn(roleId);

        Integer result = roleService.getRoleByUserId(userId);

        assertEquals(roleId, result);
        verify(roleRepository).getRoleByUserId(userId);
    }

    @Test
    void testCreateRole() {
        Role role = new Role();
        role.setName("Admin");

        when(roleRepository.save(role)).thenReturn(role);

        Role created = roleService.create(role);

        assertEquals("Admin", created.getName());
        verify(roleRepository).save(role);
    }

    @Test
    void testGetRoleByIdFound() {
        Role role = new Role();
        role.setId(1);
        role.setName("User");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        Optional<Role> found = roleService.get(1);

        assertTrue(found.isPresent());
        assertEquals("User", found.get().getName());
        verify(roleRepository).findById(1);
    }

    @Test
    void testGetRoleByIdNotFound() {
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Role> found = roleService.get(1);

        assertFalse(found.isPresent());
        verify(roleRepository).findById(1);
    }

    @Test
    void testListRoles() {
        List<Role> roles = Arrays.asList(new Role(), new Role());
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.list();

        assertEquals(2, result.size());
        verify(roleRepository).findAll();
    }

    @Test
    void testUpdateRole() {
        Role existing = new Role();
        existing.setId(1);
        existing.setName("OldName");

        Role updateData = new Role();
        updateData.setName("NewName");

        when(roleRepository.findById(1)).thenReturn(Optional.of(existing));
        when(roleRepository.save(any(Role.class))).thenAnswer(i -> i.getArgument(0));

        Role updated = roleService.update(1, updateData);

        assertEquals("NewName", updated.getName());
        verify(roleRepository).findById(1);
        verify(roleRepository).save(existing);
    }

    @Test
    void testDeleteRole() {
        doNothing().when(roleRepository).deleteById(1);

        roleService.delete(1);

        verify(roleRepository).deleteById(1);
    }

    @Test
    void testAssignPermission() {
        RolePermission rp = new RolePermission();
        rp.setRoleId(1);
        rp.setPermissionId(2);

        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(rp);

        RolePermission result = roleService.assignPermission(1, 2);

        assertEquals(1, result.getRoleId());
        assertEquals(2, result.getPermissionId());
        verify(rolePermissionRepository).save(any(RolePermission.class));
    }
}
