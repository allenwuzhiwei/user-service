package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // Enable Mockito annotations
public class PermissionControllerTest {

    @Mock
    private PermissionService permissionService;  // Mock the service

    @InjectMocks
    private PermissionController permissionController;  // Inject mocks into controller

    @Test
    public void testGetPermissionById_Found() {
        // Arrange: create a dummy permission object
        Permission permission = new Permission();
        permission.setId(1);
        permission.setEndpoint("/api/test");

        // Stub the service method to return the dummy permission wrapped in Optional
        when(permissionService.get(1)).thenReturn(Optional.of(permission));

        // Act: call the controller method
        ResponseEntity<ApiResponse<Permission>> response = permissionController.getPermissionById(1);

        // Assert: check the response status and body
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals(permission, response.getBody().getData());
    }

    @Test
    public void testGetPermissionById_NotFound() {
        // Arrange: stub the service method to return empty Optional
        when(permissionService.get(2)).thenReturn(Optional.empty());

        // Act: call the controller method with an ID that doesn't exist
        ResponseEntity<ApiResponse<Permission>> response = permissionController.getPermissionById(2);

        // Assert: check the response status and body
        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
    }
}
