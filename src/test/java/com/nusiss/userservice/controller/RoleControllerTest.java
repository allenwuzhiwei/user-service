package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchRole() {
        List<Role> roles = Arrays.asList(new Role(), new Role());
        when(roleService.list()).thenReturn(roles);

        ResponseEntity<ApiResponse<List<Role>>> response = roleController.searchRole();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    void testGetRoleById_found() {
        Role role = new Role();
        role.setId(1);
        when(roleService.get(1)).thenReturn(Optional.of(role));

        ResponseEntity<ApiResponse<Role>> response = roleController.getRoleById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().getId());
    }

    @Test
    void testGetRoleById_notFound() {
        when(roleService.get(100)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Role>> response = roleController.getRoleById(100);

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testCreateRole() {
        Role role = new Role();
        role.setName("Admin");

        when(roleService.create(role)).thenReturn(role);

        ResponseEntity<ApiResponse<Role>> response = roleController.createRole(role);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Admin", response.getBody().getData().getName());
    }

    @Test
    void testGetRoleByUserId() {
        Integer userId = 5;
        Integer roleId = 2;

        when(roleService.getRoleByUserId(userId)).thenReturn(roleId);

        ResponseEntity<ApiResponse<Integer>> response = roleController.getRoleByUserId(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals(roleId, response.getBody().getData());
    }

    @Test
    void testUpdateRole_success() {
        Role role = new Role();
        role.setName("User Updated");
        when(roleService.update(1, role)).thenReturn(role);

        ResponseEntity<ApiResponse<Role>> response = roleController.updateRole(1, role);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User Updated", response.getBody().getData().getName());
    }

    @Test
    void testUpdateRole_notFound() {
        Role role = new Role();
        when(roleService.update(100, role)).thenThrow(new RuntimeException("Role not found"));

        ResponseEntity<ApiResponse<Role>> response = roleController.updateRole(100, role);

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Role not found", response.getBody().getMessage());
    }

    @Test
    void testDeleteRole() {
        doNothing().when(roleService).delete(1);

        ResponseEntity<ApiResponse<Void>> response = roleController.deleteRole(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }
}
