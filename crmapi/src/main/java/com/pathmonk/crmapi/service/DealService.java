package com.pathmonk.crmapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pathmonk.crmapi.model.Deal;
import com.pathmonk.crmapi.repo.DealRepo;
import com.pathmonk.crmapi.web.dto.DealRequestDto;
import com.pathmonk.crmapi.web.dto.DealResponseDto;
import com.pathmonk.crmapi.web.dto.DealStageRequestDto;
import com.pathmonk.crmapi.web.dto.DealStageResponseDto;

@Service
public class DealService {
    private final DealRepo repository;

    public DealService(DealRepo repository) {
        this.repository = repository;
    }

    public Deal findByDealId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Deal createDeal(Deal deal) {
        return repository.save(deal);
    }

    public void deleteContact(Long id) {
        Deal existing = findByDealId(id);
        repository.delete(existing);
    }

    public Deal updateDeal(long dealId, DealRequestDto updatedDeal) {
        Deal deal = repository.findById(dealId).orElse(null);
  
        if (updatedDeal.getTitle() != null && !updatedDeal.getTitle().isBlank()) {
            deal.setTitle(updatedDeal.getTitle());
        }

        if (updatedDeal.getValue() != null) {
            deal.setValue(updatedDeal.getValue());
        }

        if (updatedDeal.getStage() != null) {
            deal.setStage(updatedDeal.getStage());
        }

        return repository.save(deal);
    }

    public Deal updateDealStage(long dealId, DealStageRequestDto newStage) {
        Deal deal = repository.findById(dealId).orElse(null);
        if (deal != null) {
            deal.setStage(newStage.getStage());
            return repository.save(deal);
        }
        return null;
    }

    public List<Deal> getAllDeals() {
        return repository.findAll();
    }

    public DealResponseDto toDto(Deal deal) {
        DealResponseDto dto = new DealResponseDto();
        dto.setId(deal.getId());
        dto.setTitle(deal.getTitle());
        dto.setValue(deal.getValue());
        dto.setStage(deal.getStage());
        return dto;
    }

    public DealStageResponseDto toStageDto(Deal deal) {
        DealStageResponseDto dto = new DealStageResponseDto();
        dto.setId(deal.getId());
        dto.setStage(deal.getStage());
        return dto;
    }
}
