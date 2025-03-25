// RedisCrudServiceImplTest.java
package com.nusiss.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisCrudServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private RedisCrudServiceImpl redisCrudService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void save_ShouldSaveValueWithExpiration() {
        // Arrange
        String key = "testKey";
        String value = "testValue";
        long timeout = 5;
        TimeUnit timeUnit = TimeUnit.MINUTES;

        // Act
        redisCrudService.save(key, value, timeout, timeUnit);

        // Assert
        verify(valueOperations).set(key, value, timeout, timeUnit);
    }

    @Test
    void get_ShouldReturnValue() {
        // Arrange
        String key = "testKey";
        String expectedValue = "testValue";
        when(valueOperations.get(key)).thenReturn(expectedValue);

        // Act
        String result = redisCrudService.get(key);

        // Assert
        assertEquals(expectedValue, result);
        verify(valueOperations).get(key);
    }

    @Test
    void delete_ShouldDeleteKey() {
        // Arrange
        String key = "testKey";

        // Act
        redisCrudService.delete(key);

        // Assert
        verify(redisTemplate).delete(key);
    }

    @Test
    void exists_ShouldReturnTrue_WhenKeyExists() {
        // Arrange
        String key = "testKey";
        when(redisTemplate.hasKey(key)).thenReturn(true);

        // Act
        boolean result = redisCrudService.exists(key);

        // Assert
        assertTrue(result);
        verify(redisTemplate).hasKey(key);
    }

    @Test
    void exists_ShouldReturnFalse_WhenKeyDoesNotExist() {
        // Arrange
        String key = "testKey";
        when(redisTemplate.hasKey(key)).thenReturn(false);

        // Act
        boolean result = redisCrudService.exists(key);

        // Assert
        assertFalse(result);
        verify(redisTemplate).hasKey(key);
    }
}