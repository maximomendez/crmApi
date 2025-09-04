package com.pathmonk.crmapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pathmonk.crmapi.model.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long> {
    Boolean existsByEmail(String email);
}
