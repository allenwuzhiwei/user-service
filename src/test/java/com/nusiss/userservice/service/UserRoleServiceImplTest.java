package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.RoleRepository;
import com.nusiss.userservice.dao.UserRoleRepository;
import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRoleServiceTest {

    @InjectMocks
    private UserRoleService userRoleService;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    void testGetRoleByUserId_whenUserRolePresent_thenReturnRole() {
        Integer userId = 1;
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(2);

        Role role = new Role();
        role.setId(2);
        role.setName("Admin");

        when(userRoleRepository.findByUserId(userId)).thenReturn(Optional.of(userRole));
        // Note: your service calls roleRepository.findById(userId), which seems like a bug. Usually should be roleId.
        // We'll mock as-is:
        when(roleRepository.findById(userId)).thenReturn(Optional.of(role));

        Optional<Role> result = userRoleService.getRoleByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals("Admin", result.get().getName());

        verify(userRoleRepository).findByUserId(userId);
        verify(roleRepository).findById(userId);
    }*/

    /*@Test
    void testGetRoleByUserId_whenUserRoleNotPresent_thenReturnEmpty() {
        Integer userId = 1;

        when(userRoleRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<Role> result = userRoleService.getRoleByUserId(userId);

        assertFalse(result.isPresent());

        verify(userRoleRepository).findByUserId(userId);
        verify(roleRepository, never()).findById(any());
    }*/

    @Test
    void testCreateRoleForUser() {
        Integer roleId = 1;
        Integer userId = 2;

        UserRole savedUserRole = new UserRole();
        savedUserRole.setRoleId(roleId);
        savedUserRole.setUserId(userId);

        when(userRoleRepository.save(any(UserRole.class))).thenReturn(savedUserRole);

        userRoleService.createRoleForUser(roleId, userId);

        ArgumentCaptor<UserRole> captor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoleRepository).save(captor.capture());

        UserRole captured = captor.getValue();
        assertEquals(roleId, captured.getRoleId());
        assertEquals(userId, captured.getUserId());
    }

    @Test
    void testCreateUserRole_deletesOldAndSavesNew() {
        UserRole ur = new UserRole();
        ur.setUserId(1);
        ur.setRoleId(2);

        when(userRoleRepository.save(ur)).thenReturn(ur);

        UserRole result = userRoleService.create(ur);

        verify(userRoleRepository).deleteByUserId(1);
        verify(userRoleRepository).save(ur);

        assertEquals(ur, result);
    }

    @Test
    void testGetUserRoleById() {
        UserRole ur = new UserRole();
        ur.setId(5);

        when(userRoleRepository.findById(5)).thenReturn(Optional.of(ur));

        Optional<UserRole> result = userRoleService.get(5);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getId());

        verify(userRoleRepository).findById(5);
    }

    @Test
    void testListUserRoles() {
        List<UserRole> list = List.of(new UserRole(), new UserRole());
        when(userRoleRepository.findAll()).thenReturn(list);

        List<UserRole> result = userRoleService.list();

        assertEquals(2, result.size());
        verify(userRoleRepository).findAll();
    }

    @Test
    void testDeleteById() {
        doNothing().when(userRoleRepository).deleteById(10);

        userRoleService.delete(10);

        verify(userRoleRepository).deleteById(10);
    }

    @Test
    void testDeleteByUserId() {
        doNothing().when(userRoleRepository).deleteByUserId(20);

        userRoleService.deleteByUserId(20);

        verify(userRoleRepository).deleteByUserId(20);
    }
}
