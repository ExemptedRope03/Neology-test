import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import { ParkingApiService } from '../../core/services/parking-api.service';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule,
    PageHeaderComponent
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  resettingMonth = false;

  constructor(
    private readonly api: ParkingApiService,
    private readonly snackBar: MatSnackBar
  ) {}

  startNewMonth(): void {
    const confirmed = confirm(
      'Se eliminarán todas las estancias y se reiniciarán los tiempos acumulados. ¿Continuar?'
    );

    if (!confirmed) {
      return;
    }

    this.resettingMonth = true;
    this.api
      .startMonth()
      .pipe(finalize(() => (this.resettingMonth = false)))
      .subscribe({
        next: result =>
          this.snackBar.open(
            `Nuevo mes iniciado: ${result.estanciasEliminadas} estancias eliminadas ` +
              `y ${result.residentesReiniciados} residentes reiniciados.`,
            'Cerrar',
            { duration: 5000 }
          ),
        error: error =>
          this.snackBar.open(
            error.error?.data ?? 'No fue posible iniciar un nuevo mes.',
            'Cerrar',
            { duration: 5000 }
          )
      });
  }
}
