package com.nusiss.userservice.entity;

import java.time.LocalDateTime;

public interface UserWithRolesProjection {
    Long getUserId();
    String getUsername();
    String getEmail();
    LocalDateTime getCreateDatetime();
    LocalDateTime getUpdateDatetime();
    String getCreateUser();
    String getUpdateUser();
    String getRoleName();
}
