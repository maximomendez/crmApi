package com.pathmonk.crmapi.web.dto;

public class ContactTagResponseDto {
    private Long id;
    private Long contactId;
    private Long tagId;
    private String tagName;
    private String tagType; // o enum

    // Constructor
    public ContactTagResponseDto(Long id, Long contactId, Long tagId, String tagName, String tagType) {
        this.id = id;
        this.contactId = contactId;
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagType = tagType;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}