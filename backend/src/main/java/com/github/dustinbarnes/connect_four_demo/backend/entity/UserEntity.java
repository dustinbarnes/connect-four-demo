package com.github.dustinbarnes.connect_four_demo.backend.entity;

import java.time.LocalDateTime;

public class UserEntity {
    private Long id;
    private String username;
    private String passwordHash;
    private LocalDateTime createdAt;

    public UserEntity(Long id, String username, String passwordHash, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
