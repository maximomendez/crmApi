package com.pathmonk.crmapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pathmonk.crmapi.model.Company;
import com.pathmonk.crmapi.repo.CompanyRepo;

@Service
public class CompanyService {
    private final CompanyRepo repository;

    public CompanyService(CompanyRepo repository) {
        this.repository = repository;
    }

    public boolean isNameAvailable(String name) {
        return !repository.existsByName(name);
    }

    public Company createCompany(Company company) {
        return repository.save(company);
    }

    public List<Company> searchCompanies(String query) {
        return repository.findByNameContainingIgnoreCase(query);
    }

    public List<Company> getAllCompanies() {
        return repository.findAll();
    }
}
