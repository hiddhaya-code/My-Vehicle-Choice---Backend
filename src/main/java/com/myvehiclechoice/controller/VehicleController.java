package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.*;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@Tag(name = "Vehicles", description = "Vehicle Catalogue, Trending, and Comparison Search APIs")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    @Operation(summary = "Search and filter vehicles with pagination")
    public ResponseEntity<ApiResponse<PageResponse<VehicleSummaryDto>>> getVehicles(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) VehicleCategory category,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String bodyStyle,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        VehicleFilterCriteria criteria = new VehicleFilterCriteria();
        criteria.setQuery(query);
        criteria.setCategory(category);
        criteria.setBrandId(brandId);
        criteria.setBodyStyle(bodyStyle);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setPage(page);
        criteria.setSize(size);
        criteria.setSortBy(sortBy);
        criteria.setSortDir(sortDir);

        PageResponse<VehicleSummaryDto> result = vehicleService.filterVehicles(criteria);
        return ResponseEntity.ok(ApiResponse.ok("Vehicles retrieved successfully", result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get full vehicle details, variants and specifications by vehicle ID")
    public ResponseEntity<ApiResponse<VehicleDetailDto>> getVehicleById(
            @PathVariable @Parameter(description = "Vehicle ID") Long id
    ) {
        VehicleDetailDto vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle details retrieved successfully", vehicle));
    }

    @GetMapping("/trending")
    @Operation(summary = "Get popular and trending vehicles")
    public ResponseEntity<ApiResponse<List<VehicleSummaryDto>>> getTrendingVehicles() {
        List<VehicleSummaryDto> trending = vehicleService.getTrendingVehicles();
        return ResponseEntity.ok(ApiResponse.ok("Trending vehicles retrieved successfully", trending));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured vehicles")
    public ResponseEntity<ApiResponse<List<VehicleSummaryDto>>> getFeaturedVehicles() {
        List<VehicleSummaryDto> featured = vehicleService.getFeaturedVehicles();
        return ResponseEntity.ok(ApiResponse.ok("Featured vehicles retrieved successfully", featured));
    }

    @GetMapping("/compare-search")
    @Operation(summary = "Search vehicle variants for direct comparison autocomplete")
    public ResponseEntity<ApiResponse<List<CompareSearchResultDto>>> searchForCompare(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<CompareSearchResultDto> results = vehicleService.searchVariantsForCompare(query, limit);
        return ResponseEntity.ok(ApiResponse.ok("Matching variants retrieved for comparison", results));
    }
}
