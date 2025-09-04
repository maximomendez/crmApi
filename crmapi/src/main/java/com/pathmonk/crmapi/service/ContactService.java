package com.pathmonk.crmapi.service;

import com.pathmonk.crmapi.model.Contact;
import com.pathmonk.crmapi.model.ContactTag;
import com.pathmonk.crmapi.model.Tag;
import com.pathmonk.crmapi.model.TagType;
import com.pathmonk.crmapi.repo.ContactRepo;
import com.pathmonk.crmapi.repo.ContactTagRepo;
import com.pathmonk.crmapi.web.dto.ContactRequestDto;
import com.pathmonk.crmapi.web.dto.ContactResponseDto;
import com.pathmonk.crmapi.web.dto.ContactTagResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactService {

    private final ContactRepo contactRepo;
    private final ContactTagRepo contactTagRepo;
    private final TagService tagService;

    public ContactService(ContactRepo contactRepo, ContactTagRepo contactTagRepo, TagService tagService) {
        this.contactRepo = contactRepo;
        this.contactTagRepo = contactTagRepo;
        this.tagService = tagService;
    }

    // ---------------- CRUD Contactos ----------------
    public Contact createContact(Contact contact) {
        return contactRepo.save(contact);
    }

    public List<Contact> getContacts() {
        return contactRepo.findAll();
    }

    public ContactResponseDto toDto(Contact contact) {
        List<ContactTagResponseDto> tags = contact.getContactTags().stream()
                .map(ct -> new ContactTagResponseDto(
                        ct.getId(),   
                        ct.getContact().getId(),
                        ct.getTag().getId(),
                        ct.getTag().getName(),
                        ct.getTag().getType().name()))
                .toList();

        return new ContactResponseDto(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                tags,
                contact.getVersion());
    }

    public Contact getContact(Long id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found: " + id));
    }

    public Contact updateContact(Long id, ContactRequestDto updated, Long ifMatchVersion) {
        Contact existing = getContact(id);

        System.out.println("EXISTING CONTACT: " + existing);

        // Optimistic Locking
        if (!Objects.equals(existing.getVersion(), ifMatchVersion)) {
            throw new OptimisticLockException("Version mismatch");
        }

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        return contactRepo.save(existing);
    }

    public void deleteContact(Long id) {
        Contact existing = getContact(id);
        contactRepo.delete(existing);
    }

    // ---------------- Attach / Detach Tags ----------------
    public ContactTag attachTag(Long contactId, String tagName, TagType type) {
        Contact contact = getContact(contactId);
        Tag tag = tagService.createOrGetTag(tagName, type);

        System.out.println("TAG FOR USER " + contact);
        System.out.println("TAG " + tag);

        // Verificar si ya existe la relación
        Optional<ContactTag> existingRelation = contactTagRepo.findByContactIdAndTagId(contact.getId(), tag.getId());
        System.out.println("EXISTING RELATION " + existingRelation);
        if (existingRelation.isPresent()) {
            return existingRelation.get(); // reutilizar
        }

        // Crear nueva relación
        ContactTag ct = new ContactTag();
        ct.setContact(contact);
        ct.setTag(tag);
        contact.addTag(ct); // mantiene la relación bidireccional
        return contactTagRepo.save(ct);
    }

    public void detachTag(Long contactId, Long tagId) {
        ContactTag ct = contactTagRepo.findByContactIdAndTagId(contactId, tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not attached to contact"));
        Contact contact = ct.getContact();
        contact.removeTag(ct);
        contactTagRepo.delete(ct); // solo borra la relación, no el tag
    }

    // ---------------- Búsqueda por Tags (AND) ----------------
    public Page<Contact> searchContactsByTags(List<String> tagNames, Pageable pageable) {
        if (tagNames.isEmpty()) {
            return contactRepo.findAll(pageable);
        }

        // 1️⃣ Obtener todos los Tag IDs de los nombres solicitados
        List<Tag> tags = tagNames.stream()
                .map(name -> tagService.createOrGetTag(name, TagType.USER)) // Si no existen, los crea
                .collect(Collectors.toList());
        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());

        // 2️⃣ Obtener todos los contactos que tengan cada tagId y hacer intersección
        Set<Long> contactIds = null;
        for (Long tagId : tagIds) {
            List<ContactTag> cts = contactTagRepo.findByTagId(tagId);
            Set<Long> ids = cts.stream().map(ct -> ct.getContact().getId()).collect(Collectors.toSet());
            if (contactIds == null) {
                contactIds = new HashSet<>(ids);
            } else {
                contactIds.retainAll(ids); // intersección (AND)
            }
        }

        if (contactIds == null || contactIds.isEmpty()) {
            return Page.empty(pageable);
        }

        // 3️⃣ Obtener contactos completos
        List<Contact> contacts = contactRepo.findAllById(contactIds);

        // 4️⃣ Simular paginación manual (simple)
        int start = Math.min((int) pageable.getOffset(), contacts.size());
        int end = Math.min(start + pageable.getPageSize(), contacts.size());
        List<Contact> content = contacts.subList(start, end);

        return new PageImpl<>(content, pageable, contacts.size());
    }

}