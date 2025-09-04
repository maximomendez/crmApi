package com.pathmonk.crmapi.web.dto;

import java.util.List;

public class ContactResponseDto {
    private Long id;
    private String name;
    private String email;
    private List<ContactTagResponseDto> tags; // lista de tags asociados
    private Long version;

    // Constructor completo
    public ContactResponseDto(Long id, String name, String email, List<ContactTagResponseDto> tags, Long version) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tags = tags;
        this.version = version;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<ContactTagResponseDto> getTags() { return tags; }
    public void setTags(List<ContactTagResponseDto> tags) { this.tags = tags; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}