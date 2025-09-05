package com.pathmonk.crmapi.controllers;

import com.pathmonk.crmapi.model.Company;
import com.pathmonk.crmapi.service.CompanyService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Companies", description = "Company management endpoints")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping("/check-name")
    public Map<String, Boolean> checkName(@RequestParam String name) {
        boolean available = service.isNameAvailable(name);
        return Map.of("available", available);
    }

    @PostMapping
    public Company create(@RequestBody Company company) {
        return service.createCompany(company);
    }

    @GetMapping
    public List<Company> search(@RequestParam String query) {
        return service.searchCompanies(query);
    }
}
