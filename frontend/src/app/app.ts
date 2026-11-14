import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FleetApiService } from './services/fleet-api.service';
import {
  Vehicle,
  FleetStats,
  VehicleStatus,
  ServiceType,
  VehicleHealth,
  MaintenanceRecord,
  MaintenanceCreateRequest,
} from './models/fleet.models';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private readonly api = inject(FleetApiService);

  vehicles = signal<Vehicle[]>([]);

  stats = signal<FleetStats | null>(null);

  selectedFilter = signal<VehicleStatus | 'ALL'>('ALL');

  isLoading = signal<boolean>(true);

  selectedVehicle = signal<Vehicle | null>(null);

  vehicleHealth = signal<VehicleHealth | null>(null);

  maintenanceRecords = signal<MaintenanceRecord[]>([]);

  isModalLoading = signal<boolean>(false);

  showAddForm = signal<boolean>(false);

  formError = signal<string | null>(null);

  newService: MaintenanceCreateRequest = {
    serviceDate: new Date().toISOString().split('T')[0],
    mileageAtService: 0,
    serviceType: 'OIL_CHANGE',
    cost: 0,
    description: '',
    nextServiceIntervalKm: 15000,
  };

  serviceTypes: ServiceType[] = [
    'OIL_CHANGE',
    'BRAKE_SYSTEM',
    'TIRE_REPLACEMENT',
    'GENERAL_INSPECTION',
    'BATTERY_SERVICE',
    'OTHER',
  ];

  formatLabel(value: string | null | undefined): string {
    if (!value) return '';
    return value
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(/\b\w/g, (char) => char.toUpperCase());
  }

  filteredVehicles = computed(() => {
    const filter = this.selectedFilter();
    const list = this.vehicles();

    if (filter === 'ALL') {
      return list;
    }

    return list.filter((vehicle) => vehicle.status === filter);
  });

  ngOnInit(): void {
    this.loadFleetData();
  }

  loadFleetData(): void {
    this.isLoading.set(true);

    this.api.getVehicles().subscribe({
      next: (data) => {
        this.vehicles.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      },
    });

    this.api.getFleetStats().subscribe({
      next: (data) => this.stats.set(data),
    });
  }

  setFilter(filter: VehicleStatus | 'ALL'): void {
    this.selectedFilter.set(filter);
  }

  openVehicleDetails(vehicle: Vehicle): void {
    this.formError.set(null);
    this.selectedVehicle.set(vehicle);
    this.showAddForm.set(false);
    this.newService.mileageAtService = vehicle.currentMileage;
    this.loadVehicleDetails(vehicle.id);
  }

  closeModal(): void {
    this.selectedVehicle.set(null);
    this.vehicleHealth.set(null);
    this.maintenanceRecords.set([]);
    this.showAddForm.set(false);
  }

  loadVehicleDetails(vehicleId: number): void {
    this.isModalLoading.set(true);

    this.api.getVehicleHealth(vehicleId).subscribe({
      next: (health) => this.vehicleHealth.set(health),
    });

    this.api.getMaintenanceByVehicle(vehicleId).subscribe({
      next: (records) => {
        this.maintenanceRecords.set(records);
        this.isModalLoading.set(false);
      },
      error: () => {
        this.isModalLoading.set(false);
      },
    });
  }

  toggleAddForm(): void {
    this.formError.set(null);
    this.showAddForm.update((val) => !val);
  }

  submitNewService(): void {
    const vehicle = this.selectedVehicle();

    if (!vehicle) return;

    this.api.addMaintenanceRecord(vehicle.id, this.newService).subscribe({
      next: () => {
        this.showAddForm.set(false);
        this.newService.description = '';
        this.newService.cost = 0;
        this.loadVehicleDetails(vehicle.id);
        this.loadFleetData();
      },
      error: (err) => {
        if (err.error?.validationErrors) {
          const messages = Object.values(err.error.validationErrors).join('. ');
          this.formError.set(messages);
        } else if (err.error?.message) {
          this.formError.set(err.error.message);
        } else {
          this.formError.set('Failed to save service record. Please check input data.');
        }
      },
    });
  }

  deleteServiceRecord(recordId: number): void {
    const vehicle = this.selectedVehicle();

    if (!vehicle) return;

    if (!confirm('Are you sure you want to delete this maintenance record?')) return;
    this.api.deleteMaintenanceRecord(recordId).subscribe({
      next: () => {
        this.loadVehicleDetails(vehicle.id);
        this.loadFleetData();
      },
    });
  }

  getHealthScoreClass(score: number): string {
    if (score >= 80) return 'score-good';
    if (score >= 40) return 'score-warning';
    return 'score-critical';
  }
}
