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
@RequestMapping("/contacts")
@Tag(name = "Contactos", description = "Operaciones sobre contactos")
public class ContactController {

    private final ContactService contactService;
    private final ContactRepo contactRepo;

    public ContactController(ContactService contactService, ContactRepo contactRepo) {
        this.contactService = contactService;
        this.contactRepo = contactRepo;
    }

    @Operation(
        summary = "Crear un nuevo contacto",
        description = "Crea un contacto si el email no existe previamente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Contacto creado exitosamente"),
        @ApiResponse(responseCode = "409", description = "Ya existe un contacto con ese email"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
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
        summary = "Obtener todos los contactos",
        description = "Devuelve una lista de todos los contactos."
    )
    @ApiResponse(responseCode = "200", description = "Lista de contactos")
    @GetMapping("/")
    public ResponseEntity<List<ContactResponseDto>> getAllContacts() {
        List<Contact> contacts = contactService.getContacts();
        List<ContactResponseDto> dtos = contacts.stream()
                .map(contactService::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
        summary = "Obtener un contacto por ID",
        description = "Devuelve los datos de un contacto específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contacto encontrado"),
        @ApiResponse(responseCode = "404", description = "Contacto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponseDto> getContact(
            @Parameter(description = "ID del contacto", required = true)
            @PathVariable Long id) {
        Contact contact = contactService.getContact(id);
        ContactResponseDto contactResponse = contactService.toDto(contact);
        System.out.println(contactResponse);
        return ResponseEntity.ok().eTag("\"" + contactResponse.getVersion() + "\"").body(contactResponse);
    }

    @Operation(
        summary = "Actualizar un contacto",
        description = "Actualiza los datos de un contacto existente usando control de versiones (ETag)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contacto actualizado"),
        @ApiResponse(responseCode = "404", description = "Contacto no encontrado"),
        @ApiResponse(responseCode = "412", description = "Versión desactualizada (If-Match)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponseDto> updateContact(
            @Parameter(description = "ID del contacto", required = true)
            @PathVariable Long id,
            @RequestBody ContactRequestDto contact,
            @Parameter(description = "ETag de la versión actual", required = true)
            @RequestHeader("If-Match") String ifMatch) {

        Long version = Long.parseLong(ifMatch.replace("\"", ""));
        System.out.println("VERSION ACTUAL" + version);

        Contact updated = contactService.updateContact(id, contact, version);

        ContactResponseDto updatedDto = contactService.toDto(updated);

        return ResponseEntity.ok().eTag("\"" + updated.getVersion() + "\"").body(updatedDto);
    }

    @Operation(
        summary = "Eliminar un contacto",
        description = "Elimina un contacto por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Contacto eliminado"),
        @ApiResponse(responseCode = "404", description = "Contacto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @Parameter(description = "ID del contacto", required = true)
            @PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Asociar un tag a un contacto",
        description = "Asocia un tag existente o nuevo a un contacto."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tag asociado correctamente"),
        @ApiResponse(responseCode = "404", description = "Contacto no encontrado")
    })
    @PostMapping("/{id}/tags")
    public ResponseEntity<ContactTagResponseDto> attachTag(
            @Parameter(description = "ID del contacto", required = true)
            @PathVariable Long id,
            @RequestBody TagRequestDto dto) {

        ContactTag ct = contactService.attachTag(id, dto.getName(), dto.getType());

        ContactTagResponseDto response = new ContactTagResponseDto(
                ct.getId(), // id de la relación ContactTag
                ct.getContact().getId(), // contactId
                ct.getTag().getId(), // tagId
                ct.getTag().getName(), // tagName
                ct.getTag().getType().name() // tagType
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Desasociar un tag de un contacto",
        description = "Elimina la relación entre un contacto y un tag."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tag desasociado correctamente"),
        @ApiResponse(responseCode = "404", description = "Contacto o tag no encontrado")
    })
    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Void> detachTag(
            @Parameter(description = "ID del contacto", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID del tag", required = true)
            @PathVariable Long tagId) {
        contactService.detachTag(id, tagId);
        return ResponseEntity.noContent().build();
    }
}
