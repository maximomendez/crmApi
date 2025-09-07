package com.pathmonk.crmapi.web.dto;

import com.pathmonk.crmapi.model.DealStage;
import jakarta.validation.constraints.NotNull;

public class DealStageRequestDto {
    @NotNull(message = "Stage is required")
    private DealStage stage;

    public DealStage getStage() {
        return stage;
    }

    public void setStage(DealStage stage) {
        this.stage = stage;
    }
}