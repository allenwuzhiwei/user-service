package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.UserWithRolesProjection;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<ApiResponse<List<User>>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    void testSearchUsers() {
        List<UserWithRolesProjection> projections = new ArrayList<>();
        when(userService.findUsers(anyString(), anyString(), any(Pageable.class))).thenReturn(projections);

        ResponseEntity<ApiResponse<List<UserWithRolesProjection>>> response = userController.searchUsers(
                "", "", 0, 10, "createDatetime", "desc");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetUserById_found() {
        User user = new User();
        user.setUserId(1);
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        ResponseEntity<ApiResponse<User>> response = userController.getUserById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        User userResult = response.getBody().getData();
        assertEquals(1, userResult.getUserId());
    }

    @Test
    void testGetUserById_notFound() {
        when(userService.getUserById(100)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<User>> response = userController.getUserById(100);

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);

        ResponseEntity<ApiResponse<User>> response = userController.findByUsername("testuser");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("testuser", response.getBody().getData().getUsername());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("newuser");

        when(userService.saveUser(user)).thenReturn(user);

        ResponseEntity<ApiResponse<User>> response = userController.createUser(user);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("newuser", response.getBody().getData().getUsername());
    }

    @Test
    void testUpdateUser_success() {
        User user = new User();
        user.setUsername("updatedUser");

        when(userService.updateUser(user)).thenReturn(user);

        ResponseEntity<ApiResponse<User>> response = userController.updateUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("updatedUser", response.getBody().getData().getUsername());
    }

    @Test
    void testUpdateUser_failure() {
        User user = new User();
        when(userService.updateUser(user)).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<ApiResponse<User>> response = userController.updateUser(user);

        assertEquals(500, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User not found", response.getBody().getMessage());
    }

    @Test
    void testDeleteUser_found() {
        User user = new User();
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        doNothing().when(userService).deleteUser(1);

        ResponseEntity<ApiResponse<String>> response = userController.deleteUser(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testDeleteUser_notFound() {
        when(userService.getUserById(100)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<String>> response = userController.deleteUser(100);

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
    }
}
