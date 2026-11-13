import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FleetApiService } from './services/fleet-api.service';
import { Vehicle, FleetStats } from './models/fleet.models';

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

  ngOnInit(): void {
    this.api.getVehicles().subscribe((data) => this.vehicles.set(data));
    this.api.getFleetStats().subscribe((data) => this.stats.set(data));
  }
}
