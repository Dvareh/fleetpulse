import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Vehicle,
  VehicleCreateRequest,
  VehicleUpdateRequest,
  MaintenanceRecord,
  MaintenanceCreateRequest,
  VehicleHealth,
  FleetStats,
  VehicleStatus,
} from '../models/fleet.models';

@Injectable({
  providedIn: 'root',
})
export class FleetApiService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/api';

  getVehicles(status?: VehicleStatus): Observable<Vehicle[]> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<Vehicle[]>(`${this.baseUrl}/vehicles`, { params });
  }

  getVehicleById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.baseUrl}/vehicles/${id}`);
  }

  createVehicle(payload: VehicleCreateRequest): Observable<Vehicle> {
    return this.http.post<Vehicle>(`${this.baseUrl}/vehicles`, payload);
  }

  updateVehicle(id: number, payload: VehicleUpdateRequest): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.baseUrl}/vehicles/${id}`, payload);
  }

  deleteVehicle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/vehicles/${id}`);
  }

  getMaintenanceByVehicle(vehicleId: number): Observable<MaintenanceRecord[]> {
    return this.http.get<MaintenanceRecord[]>(`${this.baseUrl}/vehicles/${vehicleId}/maintenance`);
  }

  addMaintenanceRecord(vehicleId: number, payload: MaintenanceCreateRequest,): Observable<MaintenanceRecord> {
    return this.http.post<MaintenanceRecord>(`${this.baseUrl}/vehicles/${vehicleId}/maintenance`, payload);
  }

  deleteMaintenanceRecord(recordId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/maintenance/${recordId}`);
  }

  getVehicleHealth(vehicleId: number): Observable<VehicleHealth> {
    return this.http.get<VehicleHealth>(`${this.baseUrl}/vehicles/${vehicleId}/health-check`);
  }

  getFleetStats(): Observable<FleetStats> {
    return this.http.get<FleetStats>(`${this.baseUrl}/dashboard/stats`);
  }
}
