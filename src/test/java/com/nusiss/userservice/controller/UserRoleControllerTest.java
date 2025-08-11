package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.controller.UserRoleController;
import com.nusiss.userservice.entity.UserRole;
import com.nusiss.userservice.service.UserRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRoleControllerTest {

    @InjectMocks
    private UserRoleController userRoleController;

    @Mock
    private UserRoleService userRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll_ReturnsUserRoleList() {
        UserRole userRole1 = new UserRole();
        userRole1.setUserId(1);
        userRole1.setRoleId(2);
        UserRole userRole2 = new UserRole();
        userRole2.setUserId(2);
        userRole2.setRoleId(3);
        List<UserRole> userRoles = Arrays.asList(
                userRole1,
                userRole2
        );
        when(userRoleService.list()).thenReturn(userRoles);

        ResponseEntity<ApiResponse<List<UserRole>>> response = userRoleController.getAll(0, 10);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<List<UserRole>> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("UserRoles retrieved successfully", body.getMessage());
        assertEquals(userRoles, body.getData());
        verify(userRoleService, times(1)).list();
    }

    @Test
    void testGetById_Found() {
        UserRole userRole = new UserRole();
        userRole.setUserId(1);
        userRole.setRoleId(2);
        when(userRoleService.get(1)).thenReturn(Optional.of(userRole));

        ResponseEntity<ApiResponse<UserRole>> response = userRoleController.getById(1);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<UserRole> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("UserRole found", body.getMessage());
        assertEquals(userRole, body.getData());
        verify(userRoleService).get(1);
    }

    @Test
    void testGetById_NotFound() {
        when(userRoleService.get(1)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<UserRole>> response = userRoleController.getById(1);

        assertEquals(404, response.getStatusCodeValue());
        ApiResponse<UserRole> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals("Not found", body.getMessage());
        assertNull(body.getData());
        verify(userRoleService).get(1);
    }

    @Test
    void testCreate_WithValidList() {
        UserRole ur1 = new UserRole();
        ur1.setUserId(1);
        ur1.setRoleId(2);
        UserRole ur2 = new UserRole();
        ur2.setUserId(2);
        ur2.setRoleId(3);

        when(userRoleService.create(ur1)).thenReturn(ur1);
        when(userRoleService.create(ur2)).thenReturn(ur2);

        List<UserRole> inputList = Arrays.asList(ur1, ur2);

        ResponseEntity<ApiResponse<List<UserRole>>> response = userRoleController.create(inputList);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<List<UserRole>> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("UserRole created successfully", body.getMessage());
        assertEquals(inputList, body.getData());

    }

    @Test
    void testCreate_WithEmptyList() {
        ResponseEntity<ApiResponse<List<UserRole>>> response = userRoleController.create(Collections.emptyList());

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<List<UserRole>> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("UserRole created successfully", body.getMessage());
        assertTrue(body.getData().isEmpty());

        verify(userRoleService, never()).create(any());
    }

    @Test
    void testDelete() {
        doNothing().when(userRoleService).delete(1);

        ResponseEntity<ApiResponse<Void>> response = userRoleController.delete(1);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("UserRole deleted successfully", body.getMessage());

        verify(userRoleService).delete(1);
    }
}
