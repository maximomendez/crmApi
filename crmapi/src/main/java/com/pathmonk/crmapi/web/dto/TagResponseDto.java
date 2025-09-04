package com.pathmonk.crmapi.web.dto;

import java.time.Instant;

public class TagResponseDto {

    private Long id;
    private String name;
    private String type; // USER o AUTO
    private Instant createdAt;

    // Constructor
    public TagResponseDto(Long id, String name, String type, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
