package com.pathmonk.crmapi.web.dto;

import com.pathmonk.crmapi.model.TagType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TagRequestDto {

    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 30, message = "Tag name must be 2–30 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Tag name must be lowercase letters, digits, or '-'")
    private String name;

    @NotNull(message = "Tag type is required")
    private TagType type;

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }
}
