package com.fleetpulse.api.dto;

import com.fleetpulse.api.enums.ServiceType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceResponse {

    private Long id;

    private Long vehicleId;

    private String vehicleLicensePlate;

    private LocalDate serviceDate;

    private Integer mileageAtService;

    private ServiceType serviceType;

    private BigDecimal cost;

    private String description;

    private Integer nextServiceIntervalKm;
}