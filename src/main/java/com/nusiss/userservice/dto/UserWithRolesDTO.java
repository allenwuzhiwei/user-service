package com.nusiss.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserWithRolesDTO {
    private Integer userId;
    private String username;
    private String email;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
    private String createUser;
    private String updateUser;
    private List<String> roles;

    public UserWithRolesDTO(Integer userId,
                            String username,
                            String email,
                            LocalDateTime createDatetime,
                            LocalDateTime updateDatetime,
                            String createUser,
                            String updateUser,
                            String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createDatetime = createDatetime;
        this.updateDatetime = updateDatetime;
        this.createUser = createUser;
        this.updateUser = updateUser;
        if (role != null) {
            this.roles.add(role);
        }
    }

    public Integer getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public LocalDateTime getCreateDatetime() { return createDatetime; }
    public LocalDateTime getUpdateDatetime() { return updateDatetime; }
    public String getCreateUser() { return createUser; }
    public String getUpdateUser() { return updateUser; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

}
