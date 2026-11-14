package com.fleetpulse.api.controllers;

import com.fleetpulse.api.dto.MaintenanceCreateRequest;
import com.fleetpulse.api.dto.MaintenanceResponse;
import com.fleetpulse.api.services.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Maintenance", description = "Endpoints for vehicle service logs and repairs")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/vehicles/{vehicleId}/maintenance")
    @Operation(summary = "Get all maintenance logs for a specific vehicle")
    public ResponseEntity<List<MaintenanceResponse>> getVehicleMaintenance(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(maintenanceService.getRecordsByVehicleId(vehicleId));
    }

    @GetMapping("/maintenance")
    @Operation(summary = "Get all maintenance records across entire fleet")
    public ResponseEntity<List<MaintenanceResponse>> getAllMaintenance() {
        return ResponseEntity.ok(maintenanceService.getAllRecords());
    }

    @PostMapping("/vehicles/{vehicleId}/maintenance")
    @Operation(summary = "Add a new maintenance log for a vehicle")
    public ResponseEntity<MaintenanceResponse> addMaintenanceRecord(
            @PathVariable Long vehicleId,
            @Valid @RequestBody MaintenanceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.addRecord(vehicleId, request));
    }
    
    @DeleteMapping("/maintenance/{recordId}")
    @Operation(summary = "Delete maintenance record by ID")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable Long recordId) {
        maintenanceService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }
}