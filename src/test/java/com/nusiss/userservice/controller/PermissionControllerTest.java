package com.nusiss.userservice.controller;

import com.nusiss.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionControllerTest {

@Mock
private UserService userService;

@InjectMocks
private PermissionController permissionController;

@BeforeEach
void setUp() {
MockitoAnnotations.openMocks(this);
}

/*@Test
void checkIfHasPermission_WithValidPermission_ShouldReturnTrue() {
// Arrange
String authToken = "valid-auth-token";
String url = "/api/users";
String method = "GET";
when(userService.hasPermission(authToken, url, method)).thenReturn(true);

// Act
Boolean result = permissionController.checkIfHasPermission(authToken, url, method);

// Assert
assertTrue(result);
verify(userService).hasPermission(authToken, url, method);
}

@Test
void checkIfHasPermission_WithInvalidPermission_ShouldReturnFalse() {
// Arrange
String authToken = "invalid-auth-token";
String url = "/api/admin";
String method = "POST";
when(userService.hasPermission(authToken, url, method)).thenReturn(false);

// Act
Boolean result = permissionController.checkIfHasPermission(authToken, url, method);

// Assert
assertFalse(result);
verify(userService).hasPermission(authToken, url, method);
}*/
}