package com.fleetpulse.api.services;

import com.fleetpulse.api.dto.FleetDashboardStatsResponse;
import com.fleetpulse.api.dto.VehicleHealthResponse;
import com.fleetpulse.api.entities.MaintenanceRecord;
import com.fleetpulse.api.entities.Vehicle;
import com.fleetpulse.api.enums.VehicleStatus;
import com.fleetpulse.api.repositories.MaintenanceRecordRepository;
import com.fleetpulse.api.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final VehicleRepository vehicleRepository;

    private final MaintenanceRecordRepository maintenanceRepository;

    private final VehicleService vehicleService;

    public VehicleHealthResponse calculateVehicleHealth(Long vehicleId) {

        Vehicle vehicle = vehicleService.findEntityById(vehicleId);

        List<MaintenanceRecord> records = maintenanceRepository.findByVehicleIdOrderByServiceDateDesc(vehicleId);

        BigDecimal totalCost = BigDecimal.ZERO;

        for (MaintenanceRecord record : records) {
            if (record.getCost() != null) {
                totalCost = totalCost.add(record.getCost());
            }
        }

        BigDecimal costPerKm = (vehicle.getCurrentMileage() != null && vehicle.getCurrentMileage() > 0)
                ? totalCost.divide(BigDecimal.valueOf(vehicle.getCurrentMileage()), 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        if (records.isEmpty()) {

            return VehicleHealthResponse.builder()
                    .vehicleId(vehicle.getId())
                    .licensePlate(vehicle.getLicensePlate())
                    .makeAndModel(vehicle.getMake() + " " + vehicle.getModel())
                    .currentMileage(vehicle.getCurrentMileage())
                    .healthScore(100)
                    .healthStatus("GOOD")
                    .targetServiceMileage(vehicle.getCurrentMileage() + 15000)
                    .remainingKmToService(15000)
                    .estimatedDaysRemaining(null)
                    .estimatedServiceDate(null)
                    .totalMaintenanceCost(BigDecimal.ZERO)
                    .costPerKilometer(BigDecimal.ZERO)
                    .recordedServicesCount(0)
                    .build();
        }

        MaintenanceRecord latest = records.getFirst();

        int interval = (latest.getNextServiceIntervalKm() != null) ? latest.getNextServiceIntervalKm() : 15000;

        int targetMileage = latest.getMileageAtService() + interval;

        int remainingKm = targetMileage - vehicle.getCurrentMileage();

        long dailyKm = calculateDailyKm(records, vehicle);

        Integer daysRemaining = null;

        LocalDate estimatedDate = null;

        if (dailyKm > 0 && remainingKm > 0) {

            daysRemaining = (int) (remainingKm / dailyKm);
            estimatedDate = LocalDate.now().plusDays(daysRemaining);

        } else if (remainingKm <= 0) {

            daysRemaining = 0;
            estimatedDate = LocalDate.now();
        }

        int healthScore;

        String healthStatus;

        if (remainingKm > 3000) {

            healthScore = Math.min(100, 80 + (remainingKm * 20 / interval));
            healthStatus = "GOOD";

        } else if (remainingKm > 0) {

            healthScore = Math.max(40, (remainingKm * 80 / 3000));
            healthStatus = "WARNING";

        } else {

            healthScore = Math.max(10, 40 - Math.min(30, Math.abs(remainingKm) / 100));
            healthStatus = "CRITICAL_OVERDUE";
        }
        return VehicleHealthResponse.builder()
                .vehicleId(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .makeAndModel(vehicle.getMake() + " " + vehicle.getModel())
                .currentMileage(vehicle.getCurrentMileage())
                .healthScore(healthScore)
                .healthStatus(healthStatus)
                .targetServiceMileage(targetMileage)
                .remainingKmToService(remainingKm)
                .estimatedDaysRemaining(daysRemaining)
                .estimatedServiceDate(estimatedDate)
                .totalMaintenanceCost(totalCost)
                .costPerKilometer(costPerKm)
                .recordedServicesCount(records.size())
                .build();
    }

    public FleetDashboardStatsResponse getFleetStats() {

        List<Vehicle> vehicles = vehicleRepository.findAll();

        if (vehicles.isEmpty()) {
            return FleetDashboardStatsResponse.builder()
                    .totalVehicles(0).activeVehicles(0).inServiceVehicles(0)
                    .healthyCount(0).warningCount(0).criticalCount(0)
                    .totalFleetMaintenanceCost(BigDecimal.ZERO)
                    .averageCostPerVehicle(BigDecimal.ZERO)
                    .build();
        }

        int active = (int) vehicles.stream().filter(v -> v.getStatus() == VehicleStatus.ACTIVE).count();

        int inService = (int) vehicles.stream().filter(v -> v.getStatus() == VehicleStatus.IN_SERVICE).count();

        List<VehicleHealthResponse> healthList = vehicles.stream()
                .map(v -> calculateVehicleHealth(v.getId()))
                .toList();

        int healthy = 0;
        int warning = 0;
        int critical = 0;

        BigDecimal totalFleetCost = BigDecimal.ZERO;

        for (VehicleHealthResponse healthDTO : healthList) {

            if ("GOOD".equals(healthDTO.getHealthStatus())) healthy++;
            else if ("WARNING".equals(healthDTO.getHealthStatus())) warning++;
            else critical++;

            if (healthDTO.getTotalMaintenanceCost() != null) {
                totalFleetCost = totalFleetCost.add(healthDTO.getTotalMaintenanceCost());
            }
        }

        BigDecimal avgCost = totalFleetCost.divide(BigDecimal.valueOf(vehicles.size()), 2, RoundingMode.HALF_UP);

        return FleetDashboardStatsResponse.builder()
                .totalVehicles(vehicles.size())
                .activeVehicles(active)
                .inServiceVehicles(inService)
                .healthyCount(healthy)
                .warningCount(warning)
                .criticalCount(critical)
                .totalFleetMaintenanceCost(totalFleetCost)
                .averageCostPerVehicle(avgCost)
                .build();
    }
    private long calculateDailyKm(List<MaintenanceRecord> records, Vehicle vehicle) {
        if (records.size() >= 2) {

            MaintenanceRecord newest = records.get(0);

            MaintenanceRecord oldest = records.get(records.size() - 1);

            long days = ChronoUnit.DAYS.between(oldest.getServiceDate(), newest.getServiceDate());

            long kmDiff = newest.getMileageAtService() - oldest.getMileageAtService();

            if (days > 0 && kmDiff > 0) {
                return kmDiff / days;
            }
        }
        return 45;
    }
}
