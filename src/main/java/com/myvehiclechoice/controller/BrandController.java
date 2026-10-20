package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.ApiResponse;
import com.myvehiclechoice.dto.BrandDto;
import com.myvehiclechoice.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@Tag(name = "Brands", description = "Automotive Brands API")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    @Operation(summary = "Get all active vehicle brands")
    public ResponseEntity<ApiResponse<List<BrandDto>>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllActiveBrands();
        return ResponseEntity.ok(ApiResponse.ok("Active brands retrieved successfully", brands));
    }
}
