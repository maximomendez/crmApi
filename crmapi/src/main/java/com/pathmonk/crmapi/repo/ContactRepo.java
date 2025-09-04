package com.pathmonk.crmapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pathmonk.crmapi.model.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long> {
    // Query para obtener contactos con todos los tags dados
    Boolean existsByEmail(String email);
}
