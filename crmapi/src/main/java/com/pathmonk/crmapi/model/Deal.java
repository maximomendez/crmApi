package com.pathmonk.crmapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "Deal", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 30, message = "Tag name must be 2–30 chars")
    private String title;

    @Column(name = "deal_value")
    @NotNull(message = "Value is required")
    private Double value;

    @Enumerated(EnumType.STRING)
    private DealStage stage;

    public Deal() {
    }

    public Deal(Long id, String title, Double value, DealStage stage) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.stage = stage;
    }

    // Getter y Setter
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.toLowerCase();
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
