package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.ApiResponse;
import com.myvehiclechoice.dto.auth.AuthResponse;
import com.myvehiclechoice.dto.auth.LoginRequest;
import com.myvehiclechoice.dto.auth.RegisterRequest;
import com.myvehiclechoice.dto.auth.UserProfileDto;
import com.myvehiclechoice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Authentication & User Accounts", description = "User registration, JWT login, and profile APIs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    @Operation(summary = "Register a new customer account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("Registration successful", response));
    }

    @PostMapping("/auth/login")
    @Operation(summary = "Authenticate user with email and password")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @GetMapping("/users/me")
    @Operation(summary = "Get current authenticated user profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        UserProfileDto profile = authService.getCurrentUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Profile retrieved successfully", profile));
    }
}
