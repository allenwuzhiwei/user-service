package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.RolePermission;
import com.nusiss.userservice.service.RolePermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RolePermissionControllerTest {

    @Mock
    private RolePermissionService rolePermissionService;

    @InjectMocks
    private RolePermissionController rolePermissionController;

    @Test
    public void testGetPermissionById_Found() {
        RolePermission rp = new RolePermission();
        rp.setId(1);
        when(rolePermissionService.get(1)).thenReturn(Optional.of(rp));

        ResponseEntity<ApiResponse<RolePermission>> response = rolePermissionController.getPermissionById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals(rp, response.getBody().getData());
    }

    @Test
    public void testGetPermissionById_NotFound() {
        when(rolePermissionService.get(2)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<RolePermission>> response = rolePermissionController.getPermissionById(2);

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
    }

    /*@Test
    public void testCreatePermission() {
        RolePermission rp1 = new RolePermission();
        rp1.setId(1);
        RolePermission rp2 = new RolePermission();
        rp2.setId(2);

        when(rolePermissionService.create(rp1)).thenReturn(rp1);
        when(rolePermissionService.create(rp2)).thenReturn(rp2);
//Optional<RolePermission> pr = rolePermissionService.findByRoleIdAndPermissionId(rolePermission.getRoleId(), rolePermission.getPermissionId());
        when(rolePermissionService.findByRoleIdAndPermissionId(rp2)).thenReturn(rp2);
        List<RolePermission> inputList = Arrays.asList(rp1, rp2);

        ResponseEntity<ApiResponse<List<RolePermission>>> response = rolePermissionController.createPermission(inputList);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals(0, response.getBody().getData().size());
    }*/

    @Test
    public void testCreatePermission_NullInput() {
        ResponseEntity<ApiResponse<List<RolePermission>>> response = rolePermissionController.createPermission(null);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertTrue(response.getBody().getData().isEmpty());
        verify(rolePermissionService, never()).create(any());
    }

    @Test
    public void testDeletePermission() {
        doNothing().when(rolePermissionService).delete(1);

        ResponseEntity<ApiResponse<Void>> response = rolePermissionController.deletePermission(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
        verify(rolePermissionService, times(1)).delete(1);
    }
}
