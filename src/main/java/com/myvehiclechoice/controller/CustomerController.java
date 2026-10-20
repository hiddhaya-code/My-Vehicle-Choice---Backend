package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.ApiResponse;
import com.myvehiclechoice.dto.VehicleSummaryDto;
import com.myvehiclechoice.model.entity.SavedComparison;
import com.myvehiclechoice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Customer Wishlist & Saved Features", description = "Customer private saved vehicles, comparisons, and history")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping({"/users/me/wishlist", "/users/wishlist", "/wishlist"})
    @Operation(summary = "Get current customer's saved wishlist vehicles")
    public ResponseEntity<ApiResponse<List<VehicleSummaryDto>>> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        List<VehicleSummaryDto> list = customerService.getUserWishlist(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Wishlist retrieved successfully", list));
    }

    @PostMapping({"/wishlist/{vehicleId}", "/users/wishlist/{vehicleId}"})
    @Operation(summary = "Add a vehicle to customer's wishlist")
    public ResponseEntity<ApiResponse<Void>> addToWishlist(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable Long vehicleId) {
        customerService.addToWishlist(userDetails.getUsername(), vehicleId);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle saved to wishlist", null));
    }

    @DeleteMapping({"/wishlist/{vehicleId}", "/users/wishlist/{vehicleId}"})
    @Operation(summary = "Remove a vehicle from customer's wishlist")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(@AuthenticationPrincipal UserDetails userDetails,
                                                                @PathVariable Long vehicleId) {
        customerService.removeFromWishlist(userDetails.getUsername(), vehicleId);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle removed from wishlist", null));
    }

    @GetMapping({"/users/me/comparisons", "/users/saved-comparisons"})
    @Operation(summary = "Get current customer's saved comparison records")
    public ResponseEntity<ApiResponse<List<SavedComparison>>> getSavedComparisons(@AuthenticationPrincipal UserDetails userDetails) {
        List<SavedComparison> list = customerService.getUserSavedComparisons(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Saved comparisons retrieved", list));
    }

    @PostMapping({"/comparisons/saved", "/users/saved-comparisons"})
    @Operation(summary = "Save a comparison record for future review")
    public ResponseEntity<ApiResponse<SavedComparison>> saveComparison(@AuthenticationPrincipal UserDetails userDetails,
                                                                       @RequestBody Map<String, Object> payload) {
        String title = payload.getOrDefault("title", "My Vehicle Comparison").toString();
        Object varIdsObj = payload.get("variantIds");
        String variantIds = varIdsObj != null ? varIdsObj.toString() : "";
        if (variantIds.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("variantIds is required"));
        }
        SavedComparison saved = customerService.saveComparison(userDetails.getUsername(), title, variantIds);
        return ResponseEntity.ok(ApiResponse.ok("Comparison saved successfully", saved));
    }
}
