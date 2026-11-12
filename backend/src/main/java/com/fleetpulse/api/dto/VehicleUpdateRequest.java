package com.fleetpulse.api.dto;

import com.fleetpulse.api.enums.VehicleStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleUpdateRequest {

    @NotNull(message = "Current mileage is required")
    @PositiveOrZero(message = "Mileage must be >= 0")
    private Integer currentMileage;

    @NotNull(message = "Vehicle status is required")
    private VehicleStatus status;
}