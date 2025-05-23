package com.nusiss.userservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDatetime;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDatetime;

    @Column(nullable = false)
    private String createUser;

    @Column(nullable = false)
    private String updateUser;

    @PrePersist
    protected void onCreate() {
        this.createDatetime = LocalDateTime.now();
        this.updateDatetime = LocalDateTime.now();
    }

    public User(){}

    public User(Integer userId, String username, String email,
                LocalDateTime createDatetime, LocalDateTime updateDatetime,
                String createUser, String updateUser) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createDatetime = createDatetime;
        this.updateDatetime = updateDatetime;
        this.createUser = createUser;
        this.updateUser = updateUser;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDatetime = LocalDateTime.now();
    }

}