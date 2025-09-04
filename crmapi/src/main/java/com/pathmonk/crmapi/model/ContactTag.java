package com.pathmonk.crmapi.model;

import java.time.Instant;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "contact_tags", uniqueConstraints = @UniqueConstraint(columnNames = {"contact_id","tag_id"}))
public class ContactTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private Instant createdAt = Instant.now();

    public ContactTag() {} // ✅ Constructor vacío

    public ContactTag(Long id, Contact contactId, Tag tagId, Instant createdAt) {
        this.id = id;
        this.contact = contactId;
        this.tag = tagId;
        this.createdAt = createdAt;
    }

    // ---------------- Getters ----------------
    public Long getId() {
        return id;
    }

    public Contact getContact() {
        return contact;
    }

    public Tag getTag() {
        return tag;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // ---------------- Setters ----------------
    public void setId(Long id) {
        this.id = id;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}