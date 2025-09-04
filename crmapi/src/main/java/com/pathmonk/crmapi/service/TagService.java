package com.pathmonk.crmapi.service;

import com.pathmonk.crmapi.model.Tag;
import com.pathmonk.crmapi.model.TagType;
import com.pathmonk.crmapi.repo.TagRepo;
import com.pathmonk.crmapi.repo.ContactTagRepo;
import com.pathmonk.crmapi.web.dto.TagRequestDto;
import com.pathmonk.crmapi.web.dto.TagResponseDto;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepo tagRepo;
    private final ContactTagRepo contactTagRepo;

    public TagService(TagRepo tagRepo, ContactTagRepo contactTagRepo) {
        this.tagRepo = tagRepo;
        this.contactTagRepo = contactTagRepo;
    }

    // Crear tag
    public Tag createOrGetTag(String name, TagType type) {
        return tagRepo.findByName(name)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(name.toLowerCase());
                    tag.setType(type);
                    return tagRepo.save(tag);
                });
    }


    // Obtener un tag
    public TagResponseDto getTag(Long id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + id));
        return toDto(tag);
    }

    // Listar tags con paginación
    public Page<TagResponseDto> getTags(Pageable pageable) {
        return tagRepo.findAll(pageable).map(this::toDto);
    }

    // Actualizar tag
    public TagResponseDto updateTag(Long id, TagRequestDto dto) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + id));

        tag.setName(dto.getName().toLowerCase());
        tag.setType(dto.getType());

        Tag updated = tagRepo.save(tag);
        return toDto(updated);
    }

    // Eliminar tag
    public void deleteTag(Long id) {
        if (contactTagRepo.existsById(id)) {
            throw new IllegalStateException("Tag is still attached to contacts and cannot be deleted");
        }
        tagRepo.deleteById(id);
    }

    public TagResponseDto toDto(Tag tag) {
        return new TagResponseDto(
                tag.getId(),
                tag.getName(),
                tag.getType().name(),
                tag.getCreatedAt());
    }
}
