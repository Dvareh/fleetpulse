
INSERT INTO vehicles (make, model, license_plate, production_year, current_mileage, fuel_type, status, created_at)
VALUES ('Škoda', 'Octavia', 'LU 1029A', 2021, 142500, 'DIESEL', 'ACTIVE', '2023-01-15');

INSERT INTO vehicles (make, model, license_plate, production_year, current_mileage, fuel_type, status, created_at)
VALUES ('Toyota', 'Corolla', 'LU 8821C', 2022, 68000, 'HYBRID', 'ACTIVE', '2023-03-20');

INSERT INTO vehicles (make, model, license_plate, production_year, current_mileage, fuel_type, status, created_at)
VALUES ('Volkswagen', 'Golf', 'LU 5543D', 2019, 185000, 'PETROL', 'IN_SERVICE', '2022-11-05');

INSERT INTO vehicles (make, model, license_plate, production_year, current_mileage, fuel_type, status, created_at)
VALUES ('Tesla', 'Model 3', 'LU 9901E', 2023, 31000, 'ELECTRIC', 'ACTIVE', '2023-08-10');

INSERT INTO maintenance_records (vehicle_id, service_date, mileage_at_service, service_type, cost, description, next_service_interval_km)
VALUES (1, '2024-02-10', 120000, 'OIL_CHANGE', 450.00, 'Wymiana oleju silnikowego i filtrów', 15000);

INSERT INTO maintenance_records (vehicle_id, service_date, mileage_at_service, service_type, cost, description, next_service_interval_km)
VALUES (1, '2024-06-15', 134000, 'BRAKE_SYSTEM', 820.00, 'Wymiana klocków i tarcz hamulcowych przód', 30000);

INSERT INTO maintenance_records (vehicle_id, service_date, mileage_at_service, service_type, cost, description, next_service_interval_km)
VALUES (2, '2024-04-12', 55000, 'OIL_CHANGE', 520.00, 'Przegląd okresowy ASO + olej 0W20', 15000);

INSERT INTO maintenance_records (vehicle_id, service_date, mileage_at_service, service_type, cost, description, next_service_interval_km)
VALUES (3, '2024-01-08', 170000, 'GENERAL_INSPECTION', 300.00, 'Badanie techniczne stacja kontroli pojazdów', 15000);