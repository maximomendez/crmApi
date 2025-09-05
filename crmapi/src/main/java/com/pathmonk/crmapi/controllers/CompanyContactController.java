package com.pathmonk.crmapi.controllers;
import com.pathmonk.crmapi.service.CompanyService;
import org.springframework.ui.Model;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "ContactCompany", description = "Form to contact a company")
public class CompanyContactController {
    private final CompanyService companyService;

    public CompanyContactController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/contactCompany")
    public String getContactForm(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "contact";
    }
}
