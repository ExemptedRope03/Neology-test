import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { finalize } from 'rxjs';
import { VehicleType } from '../../../../core/models/vehicle.model';
import { ParkingApiService } from '../../../../core/services/parking-api.service';
import { normalizePlate, plateValidator } from '../../../../shared/util/plate.util';

@Component({
  selector: 'app-vehicle-form-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './vehicle-form-dialog.component.html',
  styleUrl: './vehicle-form-dialog.component.css'
})
export class VehicleFormDialogComponent {
  saving = false;
  errorMessage = '';

  readonly form = new FormGroup({
    placa: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, plateValidator]
    }),
    tipo: new FormControl<VehicleType>('RESIDENT', { nonNullable: true })
  });

  constructor(
    private readonly api: ParkingApiService,
    private readonly dialogRef: MatDialogRef<VehicleFormDialogComponent>
  ) {}

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { placa, tipo } = this.form.getRawValue();
    this.saving = true;
    this.errorMessage = '';

    this.api
      .createVehicle(normalizePlate(placa), tipo)
      .pipe(finalize(() => (this.saving = false)))
      .subscribe({
        next: vehicle => this.dialogRef.close(vehicle),
        error: error => {
          this.errorMessage =
            error.error?.data ?? 'No fue posible registrar el vehículo.';
        }
      });
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
