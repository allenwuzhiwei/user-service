package com.nusiss.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.userservice.config.CustomException;
import com.nusiss.userservice.dao.AddressRepository;
import com.nusiss.userservice.dao.PermissionRepository;
import com.nusiss.userservice.dao.UserRepository;
import com.nusiss.userservice.dto.UserWithRolesDTO;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private static final String USERNAME = "testUser";

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
    void testGetCurrentUserInfo_success() throws Exception {
        // Prepare a JWT token with username "testUser"
        JwtUtils jwtUtil = new JwtUtils();
        String token = jwtUtil.generateToken(USERNAME);

        String bearerToken = "Bearer " + token;

        // Mock redisCrudService to say user exists
        when(redisCrudService.exists(USERNAME)).thenReturn(true);

        // Mock redisCrudService.get() to return JSON string
        String userJson = "{\"userId\":1,\"username\":\"testUser\"}";
        when(redisCrudService.get(USERNAME)).thenReturn(userJson);

        // Mock objectMapper to deserialize JSON to User object
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername(USERNAME);

        when(objectMapper.readValue(userJson, User.class)).thenReturn(mockUser);

        // Call method
        User user = userService.getCurrentUserInfo(bearerToken);

        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
    }

    @Test
    void testGetCurrentUserInfo_invalidToken_throwsException() {
        String invalidToken = "Bearer invalid.token";

        // Call and assert exception
        CustomException ex = assertThrows(CustomException.class, () -> {
            userService.getCurrentUserInfo(invalidToken);
        });

        assertTrue(ex.getMessage().contains("Fail to get user info"));
    }

    @Test
    void testGetCurrentUserInfo_userNotExistsInRedis_throwsException() {
        // Prepare valid JWT token with username "testUser"
        JwtUtils jwtUtil = new JwtUtils();
        String token = jwtUtil.generateToken(USERNAME);

        String bearerToken = "Bearer " + token;

        when(redisCrudService.exists(USERNAME)).thenReturn(false);

        CustomException ex = assertThrows(CustomException.class, () -> {
            userService.getCurrentUserInfo(bearerToken);
        });

        assertEquals("Fail to get user info: {}Fail to get user info: user didn't login", ex.getMessage());
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

    @Test
    void testGetUserById_UserExists() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("testUser");

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
        verify(userRepository).findById(1);
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1);

        assertFalse(result.isPresent());
        verify(userRepository).findById(1);
    }

    @Test
    void testFindByUsername() {
        User mockUser = new User();
        mockUser.setUsername("user1");

        when(userRepository.findByUsername("user1")).thenReturn(mockUser);

        User result = userService.findByUsername("user1");

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        verify(userRepository).findByUsername("user1");
    }

    /*@Test
    void testFindUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> mockUsers = Arrays.asList(new User(), new User());

        when(userRepository.searchUsersWithRoles("user", "email@example.com", pageable)).thenReturn(mockUsers);

        List<UserWithRolesDTO> results = userService.findUsers("user", "email@example.com", pageable);

        assertEquals(2, results.size());
        verify(userRepository).searchUsersWithRoles("user", "email@example.com", pageable);
    }*/

    @Test
    void testUpdateUser_Success() {
        User existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setUsername("oldUser");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword(new BCryptPasswordEncoder().encode("oldPass"));
        existingUser.setUpdateUser("admin");
        existingUser.setUpdateDatetime(LocalDateTime.now());

        User updateUser = new User();
        updateUser.setUserId(1);
        updateUser.setUsername("newUser");
        updateUser.setEmail("new@example.com");
        updateUser.setPassword("newPass");
        updateUser.setUpdateUser("updater");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updated = userService.updateUser(updateUser);

        assertEquals("newUser", updated.getUsername());
        assertEquals("new@example.com", updated.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches("newPass", updated.getPassword()));
        assertEquals("updater", updated.getUpdateUser());
        assertNotNull(updated.getUpdateDatetime());

        verify(userRepository).findById(1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_NullUserId_ThrowsException() {
        User user = new User();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(user));

        assertEquals("User ID must not be null for update.", exception.getMessage());
    }

    @Test
    void testUpdateUser_UserNotFound_ThrowsException() {
        User user = new User();
        user.setUserId(999);

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(user));

        assertEquals("User not found with ID: 999", exception.getMessage());
    }

    @Test
    void testFindUserByUsernameAndPassword() {
        List<User> mockUsers = Arrays.asList(new User());

        when(userRepository.findUserByUsernameAndPassword("user", "pass")).thenReturn(mockUsers);

        List<User> results = userService.findUserByUsernameAndPassword("user", "pass");

        assertEquals(1, results.size());
        verify(userRepository).findUserByUsernameAndPassword("user", "pass");
    }

    /*@Test
    void testSaveUser_NewUser_PasswordEncoded() {
        User newUser = new User();
        newUser.setPassword("rawPass");
        newUser.setUserId(null);

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.saveUser(newUser);

        assertNotNull(savedUser.getPassword());
        assertNotEquals("rawPass", savedUser.getPassword()); // password should be encoded
        assertEquals("System", savedUser.getCreateUser());
        assertEquals("System", savedUser.getUpdateUser());

        verify(userRepository).save(any(User.class));
    }*/

    @Test
    void testSaveUser_ExistingUser_PasswordNotEncoded() {
        User existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setPassword("encodedPass");

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.saveUser(existingUser);

        // Password remains unchanged
        assertEquals("encodedPass", savedUser.getPassword());
        assertEquals("System", savedUser.getCreateUser());
        assertEquals("System", savedUser.getUpdateUser());

        verify(userRepository).save(any(User.class));
    }

}