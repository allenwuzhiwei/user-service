package com.nusiss.userservice.entity;

import java.time.LocalDateTime;

public interface PermissionProjection {
    Integer getId();
    String getEndpoint();
    String getMethod();
    String getCreateUser();
    LocalDateTime getCreateDatetime();
    String getUpdateUser();
    LocalDateTime getUpdateDatetime();
    String getRoles();  // Comma-separated role names
}