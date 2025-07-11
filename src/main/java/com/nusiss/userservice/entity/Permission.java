package com.nusiss.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "permissions")
@Setter
@Getter
public class Permission  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private String method; // e.g., GET, POST, PUT, DELETE

    @Column(nullable = true)
    private String description;

}