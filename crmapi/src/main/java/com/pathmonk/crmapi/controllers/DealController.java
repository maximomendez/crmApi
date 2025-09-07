package com.pathmonk.crmapi.controllers;

import com.pathmonk.crmapi.model.Deal;
import com.pathmonk.crmapi.repo.DealRepo;
import com.pathmonk.crmapi.service.DealService;
import com.pathmonk.crmapi.web.dto.DealRequestDto;
import com.pathmonk.crmapi.web.dto.DealResponseDto;
import com.pathmonk.crmapi.web.dto.DealStageRequestDto;
import com.pathmonk.crmapi.web.dto.DealStageResponseDto;

import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/deals")
@Tag(name = "Deals", description = "Deals API")
@CrossOrigin(origins = "*") 
public class DealController {

    private final DealService dealService;
    private final DealRepo dealRepo;

    public DealController(DealService dealService, DealRepo dealRepo) {
        this.dealService = dealService;
        this.dealRepo = dealRepo;
    }

    @Operation(summary = "Create new deal")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contact created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<DealResponseDto> createContact(@RequestBody DealRequestDto requestDto) {
        Deal deal = new Deal();
        deal.setTitle(requestDto.getTitle());
        deal.setStage(requestDto.getStage());
        deal.setValue(requestDto.getValue());
        Deal saved = dealService.createDeal(deal);
        DealResponseDto responseDto = dealService.toDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Get all Deals", description = "Returns a list of all Deals.")
    @ApiResponse(responseCode = "200", description = "List of Deals retrieved successfully")
    @GetMapping("/")
    public ResponseEntity<List<DealResponseDto>> getAllDeals() {
        List<Deal> deals = dealService.getAllDeals();
        List<DealResponseDto> dtos = deals.stream()
                .map(dealService::toDto)
                .toList();
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get deal by ID", description = "Get a deal by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deal retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Deal not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DealResponseDto> getDeal(
            @Parameter(description = "DealId", required = true) @PathVariable Long id) {
        Deal deal = dealService.findByDealId(id);
        DealResponseDto dealResponse = dealService.toDto(deal);
        return ResponseEntity.ok(dealResponse);
    }

    @Operation(summary = "Update deal", description = "Updates an existing deal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deal updated successfully"),
            @ApiResponse(responseCode = "404", description = "Deal not found"),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<DealResponseDto> updateDeal(
            @Parameter(description = "DealId", required = true) @PathVariable Long id,
            @RequestBody DealRequestDto deal) {

        System.out.println("Updating deal with ID: " + id + " with data: " + deal);

        Deal updated = dealService.updateDeal(id, deal);

        DealResponseDto dealFDto = dealService.toDto(updated);

        System.out.println(dealFDto);

        return ResponseEntity.ok(dealFDto);
    }

    @Operation(summary = "Update deal stage", description = "Updates an existing deal stage.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deal updated successfully"),
            @ApiResponse(responseCode = "404", description = "Deal not found"),
    })
    @PatchMapping("/{id}/stage")
    public ResponseEntity<DealStageResponseDto> updateDealStage(
            @Parameter(description = "DealId", required = true) @PathVariable Long id,
            @RequestBody DealStageRequestDto deal) {

        Deal updated = dealService.updateDealStage(id, deal);

        DealStageResponseDto dealFDto = dealService.toStageDto(updated);

        return ResponseEntity.ok(dealFDto);
    }

    @Operation(summary = "Delete deal", description = "Deletes a deal by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete deal successfully"),
            @ApiResponse(responseCode = "404", description = "Deal not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(
            @Parameter(description = "DealId", required = true) @PathVariable Long id) {
        dealService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

}
