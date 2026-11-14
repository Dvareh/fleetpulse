package com.fleetpulse.api.dto;

import com.fleetpulse.api.enums.ServiceType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceCreateRequest {

    @NotNull(message = "Service date is required")
    @PastOrPresent(message = "Service date cannot be in the future")
    private LocalDate serviceDate;

    @NotNull(message = "Mileage at service is required")
    @PositiveOrZero(message = "Mileage must be >= 0")
    private Integer mileageAtService;

    @NotNull(message = "Service type is required")
    private ServiceType serviceType;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cost must be >= 0")
    private BigDecimal cost;

    @Size(max = 500, message = "Description max length is 500 characters")
    private String description;

    @Min(value = 1000, message = "Interval must be at least 1000 km")
    private Integer nextServiceIntervalKm;
}
