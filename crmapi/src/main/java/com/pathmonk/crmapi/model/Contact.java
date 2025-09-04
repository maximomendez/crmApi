package com.pathmonk.crmapi.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Version
    private Long version; // optimistic locking

    public Contact() {} // ✅ Constructor vacío requerido por JPA

    public Contact(Long id, String name, String email, Long version) { // ✅ Constructor para tu seed
        this.id = id;
        this.name = name;
        this.email = email;
        this.version = version;
    }

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContactTag> contactTags = new HashSet<>();

    // ---------------- Getters ----------------
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getVersion() {
        return version;
    }

    public Set<ContactTag> getContactTags() {
        return contactTags;
    }

    // ---------------- Setters ----------------
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setContactTags(Set<ContactTag> contactTags) {
        this.contactTags = contactTags;
    }

    // --------------- Convenience helpers ---------------
    public void addTag(ContactTag contactTag) {
        contactTags.add(contactTag);
        contactTag.setContact(this);
    }

    public void removeTag(ContactTag contactTag) {
        contactTags.remove(contactTag);
        contactTag.setContact(null);
    }
}
