package com.fleetpulse.api.repositories;

import com.fleetpulse.api.entities.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {

    List<MaintenanceRecord> findByVehicleIdOrderByServiceDateDesc(Long vehicleId);
}
