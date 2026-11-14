package com.fleetpulse.api.entities;

import com.fleetpulse.api.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    @Column(name = "mileage_at_service", nullable = false)
    private Integer mileageAtService;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = 30)
    private ServiceType serviceType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(length = 500)
    private String description;

    @Column(name = "next_service_interval_km")
    private Integer nextServiceIntervalKm;
}
