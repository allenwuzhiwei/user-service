package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Mock data
        User user1 = new User();
        user1.setUserId(1);
        user1.setUsername("testUser1");

        User user2 = new User();
        user2.setUserId(2);
        user2.setUsername("testUser2");

        List<User> users = Arrays.asList(user1, user2);

        // Mock service
        when(userService.getAllUsers()).thenReturn(users);

        // Call method
        ResponseEntity<ApiResponse<List<User>>> response = userController.getAllUsers();

        // Verify results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getData().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_UserExists() {
        // Mock data
        User user = new User();
        user.setUserId(1);
        user.setUsername("testUser");

        // Mock service
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        // Call method
        ResponseEntity<ApiResponse<User>> response = userController.getUserById(1);

        // Verify results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testUser", response.getBody().getData().getUsername());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Mock service
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        // Call method
        ResponseEntity<ApiResponse<User>> response = userController.getUserById(1);

        // Verify results
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(false, response.getBody().isSuccess());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testCreateUser() {
        // Mock data
        User user = new User();
        user.setUsername("newUser");

        User savedUser = new User();
        savedUser.setUserId(1);
        savedUser.setUsername("newUser");

        // Mock service
        when(userService.saveUser(user)).thenReturn(savedUser);

        // Call method
        ResponseEntity<ApiResponse<User>> response = userController.createUser(user);

        // Verify results
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("newUser", response.getBody().getData().getUsername());
        verify(userService, times(1)).saveUser(user);
    }

    /*@Test
    void testUpdateUser_UserExists() {
        // Mock data
        User existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setUsername("existingUser");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");

        User savedUser = new User();
        savedUser.setUserId(1);
        savedUser.setUsername("updatedUser");

        // Mock service
        when(userService.getUserById(1)).thenReturn(Optional.of(existingUser));
        when(userService.saveUser(any(User.class))).thenReturn(savedUser);

        // Call method
        ResponseEntity<ApiResponse<User>> response = userController.updateUser(1, updatedUser);

        // Verify results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("updatedUser", response.getBody().getData().getUsername());
        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).saveUser(any(User.class));
    }*/

    /*@Test
    void testUpdateUser_UserNotFound() {
        // Mock service
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        // Call method
        ResponseEntity<ApiResponse<User>> response = userController.updateUser(1, new User());

        // Verify results
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(false, response.getBody().isSuccess());
        verify(userService, times(1)).getUserById(1);
        verify(userService, never()).saveUser(any(User.class));
    }*/

    @Test
    void testDeleteUser_UserExists() {
        // Mock data
        User existingUser = new User();
        existingUser.setUserId(1);

        // Mock service
        when(userService.getUserById(1)).thenReturn(Optional.of(existingUser));
        doNothing().when(userService).deleteUser(1);

        // Call method
        ResponseEntity<ApiResponse<String>> response = userController.deleteUser(1);

        // Verify results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isSuccess());
        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Mock service
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        // Call method
        ResponseEntity<ApiResponse<String>> response = userController.deleteUser(1);

        // Verify results
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(false, response.getBody().isSuccess());
        verify(userService, times(1)).getUserById(1);
        verify(userService, never()).deleteUser(1);
    }
}