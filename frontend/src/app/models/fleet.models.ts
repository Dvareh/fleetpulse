export type FuelType = 'PETROL' | 'DIESEL' | 'ELECTRIC' | 'HYBRID';
export type VehicleStatus = 'ACTIVE' | 'IN_SERVICE' | 'DECOMMISSIONED';
export type ServiceType =
  | 'OIL_CHANGE'
  | 'BRAKE_SYSTEM'
  | 'TIRE_REPLACEMENT'
  | 'GENERAL_INSPECTION'
  | 'BATTERY_SERVICE'
  | 'OTHER';
export type HealthStatus = 'GOOD' | 'WARNING' | 'CRITICAL_OVERDUE';

export interface Vehicle {
  id: number;
  make: string;
  model: string;
  licensePlate: string;
  productionYear: number;
  currentMileage: number;
  fuelType: FuelType;
  status: VehicleStatus;
  createdAt: string;
  totalMaintenanceCount: number;
}

export interface VehicleCreateRequest {
  make: string;
  model: string;
  licensePlate: string;
  productionYear: number;
  currentMileage: number;
  fuelType: FuelType;
  status?: VehicleStatus;
}

export interface VehicleUpdateRequest {
  currentMileage: number;
  status: VehicleStatus;
}

export interface MaintenanceRecord {
  id: number;
  vehicleId: number;
  vehicleLicensePlate: string;
  serviceDate: string;
  mileageAtService: number;
  serviceType: ServiceType;
  cost: number;
  description: string;
  nextServiceIntervalKm: number;
}

export interface MaintenanceCreateRequest {
  serviceDate: string;
  mileageAtService: number;
  serviceType: ServiceType;
  cost: number;
  description: string;
  nextServiceIntervalKm?: number;
}

export interface VehicleHealth {
  vehicleId: number;
  licensePlate: string;
  makeAndModel: string;
  currentMileage: number;
  healthScore: number;
  healthStatus: HealthStatus;
  targetServiceMileage: number;
  remainingKmToService: number;
  estimatedServiceDate: string | null;
  estimatedDaysRemaining: number | null;
  totalMaintenanceCost: number;
  costPerKilometer: number;
  recordedServicesCount: number;
}

export interface FleetStats {
  totalVehicles: number;
  activeVehicles: number;
  inServiceVehicles: number;
  healthyCount: number;
  warningCount: number;
  criticalCount: number;
  totalFleetMaintenanceCost: number;
  averageCostPerVehicle: number;
}
