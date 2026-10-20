package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.ApiResponse;
import com.myvehiclechoice.dto.DealerEnquiryRequest;
import com.myvehiclechoice.dto.DealerEnquiryResponse;
import com.myvehiclechoice.dto.DealerResponseDto;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Authorized Dealers & Enquiries", description = "Where to Buy locator, Dealer Quotes & Test Drive bookings")
public class DealerController {

    private final DealerService dealerService;

    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    @GetMapping("/dealers")
    @Operation(summary = "Get authorized dealers with accurate manufacturer, category, inventory, and location matching")
    public ResponseEntity<ApiResponse<List<DealerResponseDto>>> getDealers(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Long variantId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) VehicleCategory category,
            @RequestParam(required = false) String dealerType,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String pincode,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radiusKm
    ) {
        List<DealerResponseDto> dealers = dealerService.getAccurateDealers(
                vehicleId,
                variantId,
                brandId,
                brandName,
                category,
                dealerType,
                city,
                state,
                pincode,
                latitude,
                longitude,
                radiusKm
        );
        return ResponseEntity.ok(ApiResponse.ok("Dealers retrieved successfully", dealers));
    }

    @PostMapping("/enquiries")
    @Operation(summary = "Submit a test drive or price quote enquiry")
    public ResponseEntity<ApiResponse<DealerEnquiryResponse>> createEnquiry(
            @Valid @RequestBody DealerEnquiryRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails != null ? userDetails.getUsername() : request.getCustomerEmail();
        DealerEnquiryResponse response = dealerService.createEnquiry(request, email);
        return ResponseEntity.ok(ApiResponse.ok("Enquiry submitted successfully. A dealership representative will reach out.", response));
    }

    @GetMapping("/users/me/enquiries")
    @Operation(summary = "Get authenticated user's submitted enquiries")
    public ResponseEntity<ApiResponse<List<DealerEnquiryResponse>>> getUserEnquiries(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<DealerEnquiryResponse> list = dealerService.getUserEnquiries(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("User enquiries retrieved", list));
    }
}
