package com.fleetpulse.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleHealthResponse {

    private Long vehicleId;
    private String licensePlate;
    private String makeAndModel;
    private Integer currentMileage;

    private int healthScore;
    private String healthStatus;
    private Integer targetServiceMileage;
    private Integer remainingKmToService;
    private LocalDate estimatedServiceDate;
    private Integer estimatedDaysRemaining;

    private BigDecimal totalMaintenanceCost;
    private BigDecimal costPerKilometer;
    private int recordedServicesCount;
}
