package com.fleetpulse.api.services;

import com.fleetpulse.api.dto.VehicleCreateRequest;
import com.fleetpulse.api.dto.VehicleResponse;
import com.fleetpulse.api.dto.VehicleUpdateRequest;
import com.fleetpulse.api.entities.Vehicle;
import com.fleetpulse.api.enums.VehicleStatus;
import com.fleetpulse.api.exceptions.DuplicateResourceException;
import com.fleetpulse.api.exceptions.ResourceNotFoundException;
import com.fleetpulse.api.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<VehicleResponse> getAllVehicles(VehicleStatus status) {

        List<Vehicle> list = (status != null)
                ? vehicleRepository.findByStatus(status)
                : vehicleRepository.findAll();

        return list.stream().map(this::mapToDTO).toList();
    }
    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = findEntityById(id);
        return mapToDTO(vehicle);
    }

    @Transactional
    public VehicleResponse createVehicle(VehicleCreateRequest request) {

        if (vehicleRepository.existsByLicensePlateIgnoreCase(request.getLicensePlate())) {
            throw new DuplicateResourceException("Vehicle with license plate " + request.getLicensePlate() + " already exists");
        }

        Vehicle vehicle = Vehicle.builder()
                .make(request.getMake())
                .model(request.getModel())
                .licensePlate(request.getLicensePlate().toUpperCase().trim())
                .productionYear(request.getProductionYear())
                .currentMileage(request.getCurrentMileage())
                .fuelType(request.getFuelType())
                .status(request.getStatus() != null ? request.getStatus() : VehicleStatus.ACTIVE)
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);
        return mapToDTO(saved);
    }

    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleUpdateRequest request) {

        Vehicle vehicle = findEntityById(id);

        if (request.getCurrentMileage() < vehicle.getCurrentMileage()) {
            throw new IllegalArgumentException(
                    "Updated mileage cannot be lower than current mileage (" + vehicle.getCurrentMileage() + " km)");
        }

        vehicle.setCurrentMileage(request.getCurrentMileage());
        vehicle.setStatus(request.getStatus());
        return mapToDTO(vehicle);
    }

    @Transactional
    public void deleteVehicle(Long id) {

        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle with id " + id + " not found");
        }

        vehicleRepository.deleteById(id);
    }

    public Vehicle findEntityById(Long id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }

    private VehicleResponse mapToDTO(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .licensePlate(vehicle.getLicensePlate())
                .productionYear(vehicle.getProductionYear())
                .currentMileage(vehicle.getCurrentMileage())
                .fuelType(vehicle.getFuelType())
                .status(vehicle.getStatus())
                .createdAt(vehicle.getCreatedAt())
                .totalMaintenanceCount(vehicle.getMaintenanceRecords() != null ? vehicle.getMaintenanceRecords().size() : 0)
                .build();
    }
}
