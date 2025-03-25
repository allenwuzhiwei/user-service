package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.service.RedisCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisCrudService redisCrudService;

    // Save a key-value pair with an expiration time
    @PostMapping("/{key}/{value}/{timeout}")
    public ResponseEntity<ApiResponse<String>> save(@PathVariable("key") String key, @PathVariable("value") String value, @PathVariable("timeout") Integer timeout){
        redisCrudService.save(key, value, timeout, TimeUnit.MINUTES);

        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Save successfully", ""));

    }

    // Get value by key
    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<String>> get(@PathVariable String key) {
        String value = redisCrudService.get(key);

        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Get successfully", value));
    }

    // Delete a key
    @DeleteMapping("/{key}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String key) {
        redisCrudService.delete(key);

        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Delete successfully", ""));
    }

    // Check if key exists
    @GetMapping("/exists/{key}")
    public ResponseEntity<ApiResponse<Boolean>> exists(@PathVariable String key) {
        Boolean isExists = redisCrudService.exists(key);

        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Check successfully", isExists));

    }
}
