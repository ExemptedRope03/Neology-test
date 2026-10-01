import { CurrencyPipe, DatePipe, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { forkJoin } from 'rxjs';
import { StayOverview, Vehicle, VehicleType } from '../../core/models/vehicle.model';
import { ParkingApiService } from '../../core/services/parking-api.service';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { VehicleTypeLabelPipe } from '../../shared/pipes/vehicle-type-label.pipe';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    NgIf,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatTableModule,
    PageHeaderComponent,
    VehicleTypeLabelPipe
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  readonly columns = ['placa', 'tipo', 'entrada', 'salida', 'estado', 'costo'];
  vehicles: Vehicle[] = [];
  stays: StayOverview[] = [];
  loading = true;
  errorMessage = '';

  constructor(private readonly api: ParkingApiService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.errorMessage = '';
    forkJoin({
      vehicles: this.api.getVehicles(),
      stays: this.api.getStays()
    }).subscribe({
      next: result => {
        this.vehicles = result.vehicles;
        this.stays = result.stays;
        this.loading = false;
      },
      error: error => {
        this.errorMessage =
          error.error?.data ??
          'No fue posible cargar las métricas. Reinicia el backend y verifica que esté disponible en el puerto 8080.';
        this.loading = false;
      }
    });
  }

  countByType(type: VehicleType): number {
    return this.stays.filter(vehicle => 
      vehicle.tipoVehiculo === type && vehicle.pendiente === true
    ).length;
  }

  get parkedVehicles(): number {
    return this.stays.filter(stay => stay.pendiente).length;
  }
}
