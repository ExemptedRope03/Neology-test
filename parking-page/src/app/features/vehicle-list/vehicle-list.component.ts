import { NgIf } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Router } from '@angular/router';
import { Vehicle, VehicleStatus } from '../../core/models/vehicle.model';
import { ParkingApiService } from '../../core/services/parking-api.service';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { VehicleTypeLabelPipe } from '../../shared/pipes/vehicle-type-label.pipe';
import { VehicleFormDialogComponent } from './components/dialog-create-vehicle/vehicle-form-dialog.component';

@Component({
  selector: 'app-vehicle-list',
  standalone: true,
  imports: [
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    NgIf,
    PageHeaderComponent,
    VehicleTypeLabelPipe
  ],
  templateUrl: './vehicle-list.component.html',
  styleUrl: './vehicle-list.component.css'
})
export class VehicleListComponent implements OnInit, AfterViewInit {
  readonly columns = ['placa', 'tipo', 'estado', 'acciones'];
  readonly dataSource = new MatTableDataSource<Vehicle>();
  loading = true;
  errorMessage = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private readonly api: ParkingApiService,
    private readonly router: Router,
    private readonly dialog: MatDialog
  ) {
    this.dataSource.filterPredicate = (vehicle: Vehicle, filter: string): boolean =>
      vehicle.placa.toLowerCase().includes(filter);
  }

  ngOnInit(): void {
    this.load();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  load(): void {
    this.loading = true;
    this.errorMessage = '';
    this.api.getVehicles().subscribe({
      next: vehicles => {
        this.dataSource.data = vehicles;
        this.loading = false;
      },
      error: error => {
        this.errorMessage =
          error.error?.data ??
          'No fue posible obtener los vehículos. Verifica que el backend esté activo en el puerto 8080.';
        this.loading = false;
      }
    });
  }

  filter(value: string): void {
    this.dataSource.filter = value.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  detail(plate: string): void {
    this.router.navigate(['/vehiculos', plate]);
  }

  changeStatus(vehicle: Vehicle, status: VehicleStatus): void {
    this.errorMessage = '';
    this.api.changeVehicleStatus(vehicle.placa, status).subscribe({
      next: () => this.load(),
      error: error => {
        this.errorMessage = error.error?.data ?? 'No fue posible actualizar el estado del vehículo.';
      }
    });
  }

  openCreateVehicleDialog(): void {
    this.dialog
      .open(VehicleFormDialogComponent, {
        width: '460px',
        autoFocus: false
      })
      .afterClosed()
      .subscribe(createdVehicle => {
        if (createdVehicle) {
          this.load();
        }
      });
  }
}
