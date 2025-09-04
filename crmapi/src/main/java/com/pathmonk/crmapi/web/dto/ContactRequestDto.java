package com.pathmonk.crmapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class ContactRequestDto {
    @NotBlank
    private String name;

    @Email
    private String email;

    // Constructor
    public ContactRequestDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters y setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
