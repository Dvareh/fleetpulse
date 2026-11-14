package com.fleetpulse.api.controllers;

import com.fleetpulse.api.dto.VehicleCreateRequest;
import com.fleetpulse.api.dto.VehicleResponse;
import com.fleetpulse.api.dto.VehicleUpdateRequest;
import com.fleetpulse.api.enums.VehicleStatus;
import com.fleetpulse.api.services.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Endpoints for managing company vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "Get list of all vehicles (optionally filtered by status)")
    public ResponseEntity<List<VehicleResponse>> getAllVehicles(
            @RequestParam(required = false) VehicleStatus status) {
        return ResponseEntity.ok(vehicleService.getAllVehicles(status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle details by ID")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PostMapping
    @Operation(summary = "Register a new vehicle")
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle mileage or operational status")
    public ResponseEntity<VehicleResponse> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleUpdateRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle and cascade all its maintenance records")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}