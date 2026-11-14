package com.fleetpulse.api.controllers;

import com.fleetpulse.api.dto.FleetDashboardStatsResponse;
import com.fleetpulse.api.dto.VehicleHealthResponse;
import com.fleetpulse.api.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Analytics & Predictions", description = "Predictive maintenance scheduling and TCO analytics (Creative Twist)")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/vehicles/{vehicleId}/health-check")
    @Operation(summary = "Calculate health score, TCO, and estimated next service date for a vehicle")
    public ResponseEntity<VehicleHealthResponse> getVehicleHealth(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(analyticsService.calculateVehicleHealth(vehicleId));
    }

    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get fleet-wide health and financial summary statistics")
    public ResponseEntity<FleetDashboardStatsResponse> getFleetStats() {
        return ResponseEntity.ok(analyticsService.getFleetStats());
    }
}
