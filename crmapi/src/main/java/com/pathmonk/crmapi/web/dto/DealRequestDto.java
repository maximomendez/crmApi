package com.pathmonk.crmapi.web.dto;

import com.pathmonk.crmapi.model.DealStage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DealRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 30, message = "Tag name must be 2–30 chars")
    private String title;
    
    @NotNull(message = "Value is required")
    private Double value;

    @NotNull(message = "Stage is required")
    private DealStage stage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public DealStage getStage() {
        return stage;
    }

    public void setStage(DealStage stage) {
        this.stage = stage;
    }

}