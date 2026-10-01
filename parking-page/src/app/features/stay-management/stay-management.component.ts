import { CurrencyPipe, DatePipe, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { finalize } from 'rxjs';
import { StayOverview } from '../../core/models/vehicle.model';
import { ParkingApiService } from '../../core/services/parking-api.service';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { normalizePlate, plateValidator } from '../../shared/util/plate.util';
import { VehicleTypeLabelPipe } from '../../shared/pipes/vehicle-type-label.pipe';

@Component({
  selector: 'app-stay-management',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    DatePipe,
    NgIf,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatTableModule,
    PageHeaderComponent,
    VehicleTypeLabelPipe
  ],
  templateUrl: './stay-management.component.html',
  styleUrl: './stay-management.component.css'
})
export class StayManagementComponent implements OnInit {
  savingEntry = false;
  savingExit = false;
  staysLoading = true;
  stays: StayOverview[] = [];
  staysErrorMessage = '';
  readonly columns = ['placa', 'tipo', 'entrada', 'salida', 'estado', 'costo'];

  readonly entryForm = this.plateForm();
  readonly exitForm = this.plateForm();

  constructor(
    private readonly api: ParkingApiService,
    private readonly snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadStays();
  }

  submit(kind: 'entry' | 'exit'): void {
    const form = kind === 'entry' ? this.entryForm : this.exitForm;

    if (form.invalid) {
      form.markAllAsTouched();
      return;
    }

    if (kind === 'entry') {
      this.savingEntry = true;
    } else {
      this.savingExit = true;
    }

    const plate = normalizePlate(form.controls.placa.value);
    const operation =
      kind === 'entry' ? this.api.registerEntry(plate) : this.api.registerExit(plate);

    operation
      .pipe(
        finalize(() => {
          if (kind === 'entry') {
            this.savingEntry = false;
          } else {
            this.savingExit = false;
          }
        })
      )
      .subscribe({
        next: result => {
          const charge =
            kind === 'exit' && result.importeACobrar
              ? ` Cobro: $${result.importeACobrar}.`
              : '';

          this.snackBar.open(
            `${kind === 'entry' ? 'Entrada' : 'Salida'} registrada para ${result.placa}.${charge}`,
            'Cerrar',
            { duration: 4500 }
          );
          form.reset({ placa: '' });
          this.loadStays();
        },
        error: error =>
          this.snackBar.open(
            error.error?.data ?? 'No fue posible registrar la estancia.',
            'Cerrar',
            { duration: 5000 }
          )
      });
  }

  private plateForm(): FormGroup<{ placa: FormControl<string> }> {
    return new FormGroup({
      placa: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, plateValidator]
      })
    });
  }

  loadStays(): void {
    this.staysLoading = true;
    this.staysErrorMessage = '';
    this.api.getStays().subscribe({
      next: stays => {
        this.stays = stays;
        this.staysLoading = false;
      },
      error: error => {
        this.staysErrorMessage =
          error.error?.data ??
          'No fue posible cargar las estancias. Reinicia el backend y verifica el endpoint GET /neo/estancias.';
        this.staysLoading = false;
      }
    });
  }
}
