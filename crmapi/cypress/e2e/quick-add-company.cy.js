describe("Quick Add Company Panel", () => {
  beforeEach(() => {
    cy.visit("http://localhost:8080/contactCompany");
  });

  it("should open the panel and focus on name input", () => {
    cy.get("#quickAddCompanyBtn").click();
    cy.get("#quickAddCompanyPanel").should("not.have.class", "hidden");
    cy.focused().should("have.id", "companyName");
  });

  it("should close the panel on cancel button click", () => {
    cy.get("#quickAddCompanyBtn").click();
    cy.get("#cancelQuickAdd").click();
    cy.get("#quickAddCompanyPanel").should("have.class", "hidden");
  });

  it("should close the panel when pressing Escape", () => {
    cy.get("#quickAddCompanyBtn").click();
    cy.get("body").type("{esc}");
    cy.get("#quickAddCompanyPanel").should("have.class", "hidden");
  });

  it("should show availability for new name after debounce", () => {
    cy.intercept("GET", "/api/companies/check-name*", { available: true }).as("checkName");

    cy.get("#quickAddCompanyBtn").click();
    cy.get("#companyName").type("Test Company");
    cy.wait(350);
    cy.wait("@checkName");
    cy.get("#nameHelp").should("contain.text", "✅ Available");
  });

  it("should show select link if company exists", () => {
    cy.intercept("GET", "/api/companies/check-name*", { available: false }).as("checkName");
    cy.intercept("GET", "/api/companies*?query=ExistingCompany", [
      { id: 1, name: "ExistingCompany" },
    ]).as("getCompany");

    cy.get("#quickAddCompanyBtn").click();
    cy.get("#companyName").type("ExistingCompany");
    cy.wait(350);
    cy.wait("@checkName");
    cy.get("#nameHelp").should("contain.html", "Select it");

    cy.get("#selectExisting").click();
    cy.wait("@getCompany");
    cy.get("#companySelect").should("have.value", "1");
    cy.get("#quickAddCompanyPanel").should("have.class", "hidden");
  });

  it("should validate website format", () => {
    cy.get("#quickAddCompanyBtn").click();
    cy.get("#companyWebsite").type("http://invalid.com");
    cy.get("#saveCompanyBtn").click();
    cy.get("#companyWebsite").should("have.class", "invalid");

    cy.get("#companyWebsite").clear().type("https://valid.com");
    cy.get("#saveCompanyBtn").click();
    cy.get("#companyWebsite").should("not.have.class", "invalid");
  });

  it("should create company and select it in <select>", () => {
    cy.intercept("POST", "/api/companies", {
      id: 123,
      name: "New Company"
    }).as("createCompany");

    cy.get("#quickAddCompanyBtn").click();
    cy.get("#companyName").type("New Company");
    cy.get("#companyWebsite").type("https://newcompany.com");

    cy.get("#quickAddCompanyForm").submit();
    cy.wait("@createCompany");

    cy.get("#companySelect").should("have.value", "123");
    cy.get("#quickAddCompanyPanel").should("have.class", "hidden");
    cy.get("#contactMessage").should("contain.text", "Company created successfully!");
  });

  it("should show error message if creation fails", () => {
    cy.intercept("POST", "/api/companies", { statusCode: 500 }).as("createCompanyFail");

    cy.get("#quickAddCompanyBtn").click();
    cy.get("#companyName").type("Fail Company");
    cy.get("#companyWebsite").type("https://fail.com");

    cy.get("#quickAddCompanyForm").submit();
    cy.wait("@createCompanyFail");

    cy.get("#contactMessage").should("contain.text", "Company has not been created");
    cy.get("#contactMessage").should("have.class", "alert--error");
  });
});
