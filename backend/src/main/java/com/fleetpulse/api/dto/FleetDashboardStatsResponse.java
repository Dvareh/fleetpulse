package com.fleetpulse.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FleetDashboardStatsResponse<BigDecimal> {
    private int totalVehicles;
    private int activeVehicles;
    private int inServiceVehicles;

    private int healthyCount;
    private int warningCount;
    private int criticalCount;

    private BigDecimal totalFleetMaintenanceCost;
    private BigDecimal averageCostPerVehicle;
}