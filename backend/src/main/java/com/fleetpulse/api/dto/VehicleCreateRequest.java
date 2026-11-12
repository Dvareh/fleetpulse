package com.fleetpulse.api.dto;

import com.fleetpulse.api.enums.FuelType;
import com.fleetpulse.api.enums.VehicleStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCreateRequest {

    @NotBlank(message = "Make cannot be empty")
    @Size(max = 50, message = "Make max length is 50 characters")
    private String make;

    @NotBlank(message = "Model cannot be empty")
    @Size(max = 50, message = "Model max length is 50 characters")
    private String model;

    @NotBlank(message = "License plate cannot be empty")
    @Size(max = 20, message = "License plate max length is 20 characters")
    private String licensePlate;

    @NotNull(message = "Production year is required")
    @Min(value = 1990, message = "Production year must be >= 1990")
    private Integer productionYear;

    @NotNull(message = "Current mileage is required")
    @PositiveOrZero(message = "Current mileage must be >= 0")
    private Integer currentMileage;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    private VehicleStatus status;
}