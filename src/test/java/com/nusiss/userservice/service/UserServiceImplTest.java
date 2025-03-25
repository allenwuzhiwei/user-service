package com.nusiss.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.userservice.config.CustomException;
import com.nusiss.userservice.dao.AddressRepository;
import com.nusiss.userservice.dao.PermissionRepository;
import com.nusiss.userservice.dao.UserRepository;
import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private RedisCrudService redisCrudService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PermissionRepository permissionRepository;

    private User mockUser;
    private String mockToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepare mock data
        mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("testuser");

        mockToken = "mockToken";
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(mockUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockUser, result.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userService.getUserById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());
        verify(userRepository, times(1)).findById(1);
    }


    @Test
    void testSaveUser() {
        // Arrange
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Act
        User result = userService.saveUser(mockUser);

        // Assert
        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testDeleteUser() {
        // Act
        userService.deleteUser(1);

        // Assert
        verify(userRepository, times(1)).deleteById(1);
    }
}