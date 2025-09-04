package com.pathmonk.crmapi.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 30, message = "Tag name must be 2–30 chars")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Tag name must contain only lowercase letters, digits, or '-'")
    private String name;

    @Enumerated(EnumType.STRING)
    private TagType type;

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContactTag> contactTags = new HashSet<>();

    public Tag() {}

    public Tag(Long id, String name, TagType type, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
    }

    // Getter y Setter
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name.toLowerCase(); }

    public TagType getType() { return type; }
    public void setType(TagType type) { this.type = type; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Set<ContactTag> getContactTags() { return contactTags; }
    public void setContactTags(Set<ContactTag> contactTags) { this.contactTags = contactTags; }
}
