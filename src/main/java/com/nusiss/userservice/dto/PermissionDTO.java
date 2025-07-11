package com.nusiss.userservice.dto;

import com.nusiss.userservice.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PermissionDTO extends BaseEntity {
    private Integer id;
    private String endpoint;
    private String method;
    private String createUser;
    private String description;
    private LocalDateTime createDatetime;
    private String updateUser;
    private LocalDateTime updateDatetime;
    private List<String> roles;  // new!
}
