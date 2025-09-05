package com.pathmonk.crmapi.repo;

import com.pathmonk.crmapi.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompanyRepo extends JpaRepository<Company, Long> {
    boolean existsByName(String name);
    List<Company> findByNameContainingIgnoreCase(String query);
}