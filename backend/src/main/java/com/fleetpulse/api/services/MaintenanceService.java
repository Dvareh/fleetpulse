package com.fleetpulse.api.services;

import com.fleetpulse.api.dto.MaintenanceCreateRequest;
import com.fleetpulse.api.dto.MaintenanceResponse;
import com.fleetpulse.api.entities.MaintenanceRecord;
import com.fleetpulse.api.entities.Vehicle;
import com.fleetpulse.api.exceptions.ResourceNotFoundException;
import com.fleetpulse.api.repositories.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceService {

    private final MaintenanceRecordRepository maintenanceRepository;

    private final VehicleService vehicleService;

    public List<MaintenanceResponse> getRecordsByVehicleId(Long vehicleId) {
        vehicleService.findEntityById(vehicleId);
        return maintenanceRepository.findByVehicleIdOrderByServiceDateDesc(vehicleId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<MaintenanceResponse> getAllRecords() {
        return maintenanceRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public MaintenanceResponse addRecord(Long vehicleId, MaintenanceCreateRequest request) {

        Vehicle vehicle = vehicleService.findEntityById(vehicleId);

        if (request.getMileageAtService() > vehicle.getCurrentMileage()) {
            vehicle.setCurrentMileage(request.getMileageAtService());
        }

        MaintenanceRecord record = MaintenanceRecord.builder()
                .vehicle(vehicle)
                .serviceDate(request.getServiceDate())
                .mileageAtService(request.getMileageAtService())
                .serviceType(request.getServiceType())
                .cost(request.getCost())
                .description(request.getDescription())
                .nextServiceIntervalKm(request.getNextServiceIntervalKm() != null ? request.getNextServiceIntervalKm() : 15000)
                .build();

        MaintenanceRecord saved = maintenanceRepository.save(record);
        return mapToDTO(saved);
    }

    @Transactional
    public void deleteRecord(Long recordId) {

        if (!maintenanceRepository.existsById(recordId)) {
            throw new ResourceNotFoundException("Maintenance record not found with id: " + recordId);
        }

        maintenanceRepository.deleteById(recordId);
    }

    private MaintenanceResponse mapToDTO(MaintenanceRecord record) {
        return MaintenanceResponse.builder()
                .id(record.getId())
                .vehicleId(record.getVehicle().getId())
                .vehicleLicensePlate(record.getVehicle().getLicensePlate())
                .serviceDate(record.getServiceDate())
                .mileageAtService(record.getMileageAtService())
                .serviceType(record.getServiceType())
                .cost(record.getCost())
                .description(record.getDescription())
                .nextServiceIntervalKm(record.getNextServiceIntervalKm())
                .build();
    }
}
