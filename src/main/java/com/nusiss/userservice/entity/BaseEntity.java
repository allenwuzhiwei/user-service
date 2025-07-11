package com.nusiss.userservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Singapore")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Singapore")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_user")
    private String updateUser;

    @PrePersist
    public void prePersist() {
        // Set the createDatetime to the current timestamp
        this.createDatetime = LocalDateTime.now();
        this.updateDatetime = this.createDatetime;
        // Set the user who is performing the operation (can be fetched from the security context or authentication)
        this.createUser = "system";
        this.updateUser = this.createUser; // or leave it as null if you prefer to set it later
    }

    @PreUpdate
    public void preUpdate() {
        // Set the updateDatetime to the current timestamp
        this.updateDatetime = LocalDateTime.now();

        // Optionally, set the user who is performing the update
        this.updateUser = "system";
    }


}
