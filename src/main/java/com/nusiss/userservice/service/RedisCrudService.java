package com.nusiss.userservice.service;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public interface RedisCrudService {

    // Save a key-value pair with expiration time
    void save(String key, String value, long timeout, TimeUnit timeUnit);

    // Retrieve value by key
    String get(String key);

    // Delete a key
    void delete(String key);

    // Check if a key exists
    boolean exists(String key);
}
