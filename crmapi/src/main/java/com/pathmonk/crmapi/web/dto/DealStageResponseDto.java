package com.pathmonk.crmapi.web.dto;

import com.pathmonk.crmapi.model.DealStage;

public class DealStageResponseDto {
    private Long id;
    private DealStage stage;

    public DealStage getStage() {
        return stage;
    }

    public void setStage(DealStage stage) {
        this.stage = stage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
