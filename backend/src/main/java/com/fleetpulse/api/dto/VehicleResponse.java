package com.fleetpulse.api.dto;

import com.fleetpulse.api.enums.FuelType;
import com.fleetpulse.api.enums.VehicleStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {

    private Long id;

    private String make;

    private String model;

    private String licensePlate;

    private Integer productionYear;

    private Integer currentMileage;

    private FuelType fuelType;

    private VehicleStatus status;

    private LocalDate createdAt;

    private int totalMaintenanceCount;

}