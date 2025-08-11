package com.nusiss.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.userservice.config.CustomException;
import com.nusiss.userservice.dao.UserRepository;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
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
    private JwtUtils jwtUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testLogin_InvalidUsernamePassword_ShouldThrowException() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword("test");
        when(userService.findByUsername(username)).thenReturn(user);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            loginService.login(username, password);
        });
        assertEquals("Login unsuccessfully", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testLogin_CorrectUsernamePassword() {
        // Arrange
        String username = "admin";
        String password = "password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword("$2a$10$RgOBjYIZ3wHfFvZYq3NdKe/Fr8EIRUoizWJM1DM0j00bBHzulakpa");
        when(userService.findByUsername(username)).thenReturn(user);

        when(jwtUtil.generateToken(username)).thenReturn("token");
        String token = loginService.login(username, password);
        assertEquals("token", token);
    }

    @Test
    void testLogin_CorrectUsernamePassword_ShouldThrowException() throws JsonProcessingException {
        // Arrange
        String username = "admin";
        String password = "password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword("$2a$10$RgOBjYIZ3wHfFvZYq3NdKe/Fr8EIRUoizWJM1DM0j00bBHzulakpa");
        when(userService.findByUsername(username)).thenReturn(user);

        when(jwtUtil.generateToken(username)).thenReturn("token");


        // This is a checked exception, so your test method must declare `throws Exception`
        /*doThrow(new RuntimeException("Redis error"))
                .when(redisCrudService)
                .save(anyString(), anyString(), anyLong(), any(TimeUnit.class));*/
        when(objectMapper.writeValueAsString(user))
                .thenThrow(new JsonProcessingException("Serialization failed") {});
        try{
            loginService.login(username, password);
        }catch (Exception e){
            assertTrue(e.getMessage().contains("Serialization failed"));

        }
    }

    @Test
    void testValidateToken_TokenExistsInRedis_ShouldReturnTrue() {
        String token = "Bearer valid.token.value";
        String username = "admin";

        when(jwtUtil.extractUserName("valid.token.value")).thenReturn(username);
        when(redisCrudService.exists(username)).thenReturn(true);

        boolean isValid = loginService.validateToken(token);

        assertTrue(isValid, "Token should be valid when present in Redis");
    }

    @Test
    void testValidateToken_TokenDoesNotExistInRedis_ShouldReturnFalse() {
        String token = "Bearer some.token.value";
        String username = "user";

        when(jwtUtil.extractUserName("some.token.value")).thenReturn(username);
        when(redisCrudService.exists(username)).thenReturn(false);

        boolean isValid = loginService.validateToken(token);

        assertFalse(isValid, "Token should be invalid when not present in Redis");
    }

    @Test
    void testGetExpiredDateTime() throws ParseException {
        LoginServiceImpl instance = new LoginServiceImpl();

        String expiredDateTimeStr = instance.getExpiredDateTime();

        // Parse the returned string back to a Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expiredDate = sdf.parse(expiredDateTimeStr);

        // Get current time plus 30 minutes
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.add(Calendar.MINUTE, 30);
        Date expectedDate = expectedCalendar.getTime();

        // Allow some delta (e.g., 2 seconds) because time passes during test
        long diffMillis = Math.abs(expiredDate.getTime() - expectedDate.getTime());

        assertTrue(diffMillis < 2000, "Expired date should be approximately 30 minutes from now");
    }



}