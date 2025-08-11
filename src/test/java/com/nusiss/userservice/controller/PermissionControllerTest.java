package com.nusiss.userservice.controller;


import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.dto.PermissionDTO;
import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.service.PermissionService;
import com.nusiss.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionControllerTest {
    @Mock
    private PermissionService permissionService;

    @Mock
    private UserService userService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private RequestMappingHandlerMapping handlerMapping;

    @InjectMocks
    private PermissionController permissionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class))
                .thenReturn(handlerMapping);
        permissionController = new PermissionController(applicationContext);
        permissionController.permissionService = permissionService;
        permissionController.userService = userService;
    }

    @Test
    void testGetPermissionById_found() {
        Permission p = new Permission();
        p.setId(1);
        p.setEndpoint("/test");
        when(permissionService.get(1)).thenReturn(Optional.of(p));

        ResponseEntity<ApiResponse<Permission>> response = permissionController.getPermissionById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("/test", response.getBody().getData().getEndpoint());
    }

    @Test
    void testGetPermissionById_notFound() {
        when(permissionService.get(100)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Permission>> response = permissionController.getPermissionById(100);

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testCreatePermission() {
        Permission dto = new Permission();
        dto.setEndpoint("/create");
        dto.setMethod("POST");

        when(permissionService.create(dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<Permission>> response = permissionController.createPermission(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("/create", response.getBody().getData().getEndpoint());
    }

    @Test
    void testDeletePermission() {
        doNothing().when(permissionService).delete(1);

        ResponseEntity<ApiResponse<Void>> response = permissionController.deletePermission(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testCheckIfHasPermission_true() {
        when(userService.hasPermission("validToken", "/api/orders", "GET")).thenReturn(true);

        Boolean result = permissionController.checkIfHasPermission("validToken", "/api/orders", "GET");

        assertTrue(result);
    }

    @Test
    void testGetAllPermissions() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page emptyPage = new PageImpl<>(Collections.emptyList());

        when(permissionService.getAllPermissionsDTO("", pageable)).thenReturn(emptyPage);

        ResponseEntity<ApiResponse<Page<PermissionDTO>>> response = permissionController.getAllPermissions(0, 10, "createDatetime", "", "desc");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }
}