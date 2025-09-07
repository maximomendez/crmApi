package com.pathmonk.crmapi.controllers;

import com.pathmonk.crmapi.model.Contact;
import com.pathmonk.crmapi.model.ContactTag;
import com.pathmonk.crmapi.repo.ContactRepo;
import com.pathmonk.crmapi.service.ContactService;
import com.pathmonk.crmapi.web.dto.ContactRequestDto;
import com.pathmonk.crmapi.web.dto.ContactResponseDto;
import com.pathmonk.crmapi.web.dto.TagRequestDto;
import com.pathmonk.crmapi.web.dto.ContactTagResponseDto;

import jakarta.persistence.EntityExistsException;

import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contacts", description = "Contact management endpoints")
public class ContactController {

    private final ContactService contactService;
    private final ContactRepo contactRepo;

    public ContactController(ContactService contactService, ContactRepo contactRepo) {
        this.contactService = contactService;
        this.contactRepo = contactRepo;
    }

    @Operation(
        summary = "Create new contact",
        description = "Creates a new contact if the email does not already exist."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Contact created successfully"),
        @ApiResponse(responseCode = "409", description = "Contact already exists"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ContactResponseDto> createContact(@RequestBody ContactRequestDto requestDto) {
        if (contactRepo.existsByEmail(requestDto.getEmail())) {
            throw new EntityExistsException("Contact already exists with email: " + requestDto.getEmail());
        }
        Contact contact = new Contact();
        contact.setName(requestDto.getName());
        contact.setEmail(requestDto.getEmail());
        Contact saved = contactService.createContact(contact);
        ContactResponseDto responseDto = contactService.toDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(
        summary = "Get all contacts",
        description = "Returns a list of all contacts."
    )
    @ApiResponse(responseCode = "200", description = "List of contacts retrieved successfully")
    @GetMapping("/")
    public ResponseEntity<List<ContactResponseDto>> getAllContacts() {
        List<Contact> contacts = contactService.getContacts();
        List<ContactResponseDto> dtos = contacts.stream()
                .map(contactService::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
        summary = "Get contact by ID",
        description = "Get a contact by its ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contact retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponseDto> getContact(
            @Parameter(description = "ContactId", required = true)
            @PathVariable Long id) {
        Contact contact = contactService.getContact(id);
        ContactResponseDto contactResponse = contactService.toDto(contact);
        return ResponseEntity.ok().eTag("\"" + contactResponse.getVersion() + "\"").body(contactResponse);
    }

    @Operation(
        summary = "Update contact",
        description = "Updates an existing contact. Requires the If-Match header for optimistic locking."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contact updated successfully"),
        @ApiResponse(responseCode = "404", description = "Contact not found"),
        @ApiResponse(responseCode = "412", description = "Precondition Failed - Version mismatch"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponseDto> updateContact(
            @Parameter(description = "ContactId", required = true)
            @PathVariable Long id,
            @RequestBody ContactRequestDto contact,
            @Parameter(description = "ETag current version", required = true)
            @RequestHeader("If-Match") String ifMatch) {

        Long version = Long.parseLong(ifMatch.replace("\"", ""));

        Contact updated = contactService.updateContact(id, contact, version);

        ContactResponseDto updatedDto = contactService.toDto(updated);

        return ResponseEntity.ok().eTag("\"" + updated.getVersion() + "\"").body(updatedDto);
    }

    @Operation(
        summary = "Delete contact",
        description = "Deletes a contact by its ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Delete contact successfully"),
        @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @Parameter(description = "ContactId", required = true)
            @PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Tag a contact",
        description = "Put a tag on a contact. If the tag does not exist, it will be created."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tag attached successfully"),
        @ApiResponse(responseCode = "404", description = "Contact not found"),
    })
    @PostMapping("/{id}/tags")
    public ResponseEntity<ContactTagResponseDto> attachTag(
            @Parameter(description = "ContactId", required = true)
            @PathVariable Long id,
            @RequestBody TagRequestDto dto) {

        ContactTag ct = contactService.attachTag(id, dto.getName(), dto.getType());

        ContactTagResponseDto response = new ContactTagResponseDto(
                ct.getId(),
                ct.getContact().getId(),
                ct.getTag().getId(),
                ct.getTag().getName(),
                ct.getTag().getType().name()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Detach tag from contact",
        description = "Deletes the association of a tag from a contact."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tag detached successfully"),
        @ApiResponse(responseCode = "404", description = "Contact or Tag not found"),
    })
    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Void> detachTag(
            @Parameter(description = "ContactId", required = true)
            @PathVariable Long id,
            @Parameter(description = "TagId", required = true)
            @PathVariable Long tagId) {
        contactService.detachTag(id, tagId);
        return ResponseEntity.noContent().build();
    }
}
