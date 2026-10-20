package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@Tag(name = "Health & Diagnostics", description = "Service heartbeat and readiness check")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "Root ping endpoint for cloud platforms")
    public ResponseEntity<Map<String, Object>> root() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "MyVehicleChoice Backend API",
                "version", "0.0.1-SNAPSHOT",
                "timestamp", Instant.now().toString()
        ));
    }

    @GetMapping("/api/v1/health")
    @Operation(summary = "Application health check")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        return ResponseEntity.ok(ApiResponse.ok("Service is healthy and ready", Map.of(
                "status", "UP",
                "database", "CONNECTED",
                "timestamp", Instant.now().toString()
        )));
    }
}
