$(document).ready(function () {
  const panel = $("#quickAddCompanyPanel");
  const btn = $("#quickAddCompanyBtn");
  const cancelBtn = $("#cancelQuickAdd, #cancelQuickAddForm");
  const nameInput = $("#companyName");
  const websiteInput = $("#companyWebsite");
  const saveBtn = $("#saveCompanyBtn");
  const nameHelp = $("#nameHelp");
  const companySelect = $("#companySelect");

  let debounceTimer;

  function openPanel() {
    panel.removeClass("hidden").attr("aria-hidden", "false");
    nameInput.focus();
  }

  function closePanel() {
    panel.addClass("hidden").attr("aria-hidden", "true");
    btn.focus();
    $("#quickAddCompanyForm")[0].reset();
    nameHelp.text("");
  }

  btn.click(openPanel);
  cancelBtn.click(closePanel);

  $(document).on("keydown", function (e) {
    if (e.key === "Escape" && !panel.hasClass("hidden")) {
      closePanel();
    }
  });

  // Debounced name check
  nameInput.on("keyup", function () {
    clearTimeout(debounceTimer);
    const name = nameInput.val().trim();
    if (!name) {
      nameHelp.text("");
      return;
    }
    debounceTimer = setTimeout(() => {
      $.get("/api/companies/check-name", { name }, function (data) {
        if (data.available) {
          nameHelp.text("✅ Available");
        } else {
          nameHelp.html(
            '❌ A company with this name exists. <a href="#" id="selectExisting">Select it</a>'
          );
          $("#selectExisting").click(function (e) {
            e.preventDefault();
            $.get("/api/companies", { query: name }, function (companies) {
              if (companies.length > 0) {
                companySelect.val(companies[0].id);
                closePanel();
              }
            });
          });
        }
      });
    }, 300);
  });

  // Website validation
  function validateWebsite() {
    const url = websiteInput.val().trim();
    if (url && !/^https:\/\/.+/.test(url)) {
      websiteInput.addClass("invalid");
      return false;
    } else {
      websiteInput.removeClass("invalid");
      return true;
    }
  }

  websiteInput.on("input", validateWebsite);

  function showMessage(message, error) {
    const msgDiv = $("#contactMessage");

    msgDiv.removeClass("alert--success alert--error");

    if (!error) {
      msgDiv.addClass("alert--success");
    } else {
      msgDiv.addClass("alert--error");
    }

    msgDiv.text(message).fadeIn();

    setTimeout(() => {
      msgDiv.fadeOut();
    }, 3000);
  }

  // Submit form
  $("#quickAddCompanyForm").submit(function (e) {
    e.preventDefault();
    if (!nameInput.val().trim() || !validateWebsite()) return;

    saveBtn.prop("disabled", true);
    $(".spinner").removeClass("hidden");

    const newCompany = {
      name: nameInput.val().trim(),
      website: websiteInput.val().trim(),
      industry: $("#companyIndustry").val().trim(),
    };

    $.ajax({
      url: "/api/companies",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(newCompany),
      success: function (company) {
        companySelect.append(
          `<option value="${company.id}" selected>${company.name}</option>`
        );
        closePanel();
        showMessage("Company created successfully!", false);
      },
      error: function () {
        showMessage("Company has not been created", true);
      },
      complete: function () {
        saveBtn.prop("disabled", false);
        $(".spinner").addClass("hidden");
      },
    });
  });

  // Enter key triggers save
  $("#quickAddCompanyForm input").keypress(function (e) {
    if (e.which === 13) {
      e.preventDefault();
      saveBtn.click();
    }
  });
});
