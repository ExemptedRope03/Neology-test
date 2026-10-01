import { CurrencyPipe, DatePipe, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { VehicleDetail } from '../../core/models/vehicle.model';
import { ParkingApiService } from '../../core/services/parking-api.service';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { VehicleTypeLabelPipe } from '../../shared/pipes/vehicle-type-label.pipe';

@Component({
  selector: 'app-vehicle-detail',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    NgIf,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatTableModule,
    PageHeaderComponent,
    VehicleTypeLabelPipe
  ],
  templateUrl: './vehicle-detail.component.html',
  styleUrl: './vehicle-detail.component.css'
})
export class VehicleDetailComponent implements OnInit {
  vehicle?: VehicleDetail;
  error = false;
  readonly columns = ['entrada', 'salida', 'estado'];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly api: ParkingApiService
  ) {}

  ngOnInit(): void {
    const plate = this.route.snapshot.paramMap.get('placa');

    if (plate) {
      this.api.getVehicle(plate).subscribe({
        next: vehicle => (this.vehicle = vehicle),
        error: () => (this.error = true)
      });
    }
  }
}
