package com.pathmonk.crmapi.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pathmonk.crmapi.model.ContactTag;

@Repository
public interface ContactTagRepo extends JpaRepository<ContactTag, Long> {

    // Obtener todas las relaciones de tags de un contacto
    List<ContactTag> findByContactId(Long contactId);

    // Buscar relación específica contact-tag
    Optional<ContactTag> findByContactIdAndTagId(Long contactId, Long tagId);

    // Borrar relación específica contact-tag
    void deleteByContactIdAndTagId(Long contactId, Long tagId);

    // Búsqueda de ContactTag por tagId (opcional para búsquedas AND)
    List<ContactTag> findByTagId(Long tagId);
}