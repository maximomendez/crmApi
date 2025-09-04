package com.pathmonk.crmapi.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pathmonk.crmapi.model.Tag;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {
    // Buscar un tag por nombre (normalizado) para reusar tags existentes
    Optional<Tag> findByName(String name);
}