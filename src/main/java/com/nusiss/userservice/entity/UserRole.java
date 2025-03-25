package com.nusiss.userservice.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_roles", schema = "nusmall_user")
public class UserRole {
    @EmbeddedId
    private UserRoleId id;

}