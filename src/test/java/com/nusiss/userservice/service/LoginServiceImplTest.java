package com.nusiss.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.userservice.config.CustomException;
import com.nusiss.userservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private RedisCrudService redisCrudService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    /*@Test
    void testLogin_InvalidUsernamePassword_ShouldThrowException() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";
        List<User> users = Arrays.asList(); // No user found

        when(userService.findUserByUsernameAndPassword(username, password)).thenReturn(users);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            loginService.login(username, password);
        });
        assertEquals("Invalid username/password.", exception.getMessage(), "Exception message should match");
    }*/




}