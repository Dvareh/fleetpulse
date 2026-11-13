import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FleetApiService } from './services/fleet-api.service';
import { Vehicle, FleetStats, VehicleStatus } from './models/fleet.models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private readonly api = inject(FleetApiService);

  vehicles = signal<Vehicle[]>([]);
  stats = signal<FleetStats | null>(null);
  selectedFilter = signal<VehicleStatus | 'ALL'>('ALL');
  isLoading = signal<boolean>(true);

  filteredVehicles = computed(() => {

    const filter = this.selectedFilter();
    const list = this.vehicles();

    if (filter === 'ALL') {
      return list;
    }

    return list.filter((v) => v.status === filter);
  });

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {

    this.isLoading.set(true);

    this.api.getVehicles().subscribe({
      next: (data) => {

        this.vehicles.set(data);
        this.isLoading.set(false);

      },
      error: (err) => {

        console.error('Failed to load vehicles', err);
        this.isLoading.set(false);

      },
    });

    this.api.getFleetStats().subscribe({
      next: (data) => this.stats.set(data),
      error: (err) => console.error('Failed to load stats', err),
    });
  }

  setFilter(filter: VehicleStatus | 'ALL'): void {
    this.selectedFilter.set(filter);
  }

  getFuelBadgeClass(fuelType: string): string {
    switch (fuelType) {
      case 'ELECTRIC':
        return 'badge-electric';
      case 'HYBRID':
        return 'badge-hybrid';
      case 'DIESEL':
        return 'badge-diesel';
      default:
        return 'badge-petrol';
    }
  }
}
