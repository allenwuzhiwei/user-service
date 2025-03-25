// RedisControllerTest.java
package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.service.RedisCrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisControllerTest {

    @Mock
    private RedisCrudService redisCrudService;

    @InjectMocks
    private RedisController redisController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldReturnSuccessResponse() {
        // Arrange
        String key = "testKey";
        String value = "testValue";
        Integer timeout = 5;

        // Act
        ResponseEntity<ApiResponse<String>> response = redisController.save(key, value, timeout);

        // Assert
        verify(redisCrudService).save(key, value, timeout, TimeUnit.MINUTES);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Save successfully", response.getBody().getMessage());
    }

    @Test
    void get_ShouldReturnValueAndSuccessResponse() {
        // Arrange
        String key = "testKey";
        String expectedValue = "testValue";
        when(redisCrudService.get(key)).thenReturn(expectedValue);

        // Act
        ResponseEntity<ApiResponse<String>> response = redisController.get(key);

        // Assert
        verify(redisCrudService).get(key);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Get successfully", response.getBody().getMessage());
        assertEquals(expectedValue, response.getBody().getData());
    }

    @Test
    void delete_ShouldReturnSuccessResponse() {
        // Arrange
        String key = "testKey";

        // Act
        ResponseEntity<ApiResponse<String>> response = redisController.delete(key);

        // Assert
        verify(redisCrudService).delete(key);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Delete successfully", response.getBody().getMessage());
    }

    @Test
    void exists_ShouldReturnExistenceStatusAndSuccessResponse() {
        // Arrange
        String key = "testKey";
        when(redisCrudService.exists(key)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse<Boolean>> response = redisController.exists(key);

        // Assert
        verify(redisCrudService).exists(key);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Check successfully", response.getBody().getMessage());
        assertTrue(response.getBody().getData());
    }
}

