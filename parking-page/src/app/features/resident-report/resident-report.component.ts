import { CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { PaymentReport, VehicleType } from '../../core/models/vehicle.model';
import { ParkingApiService } from '../../core/services/parking-api.service';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { VehicleTypeLabelPipe } from '../../shared/pipes/vehicle-type-label.pipe';

type ReportFilter = 'ALL' | VehicleType;

@Component({
  selector: 'app-resident-report',
  standalone: true,
  imports: [
    CurrencyPipe,
    NgFor,
    NgIf,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatSelectModule,
    MatTableModule,
    PageHeaderComponent,
    VehicleTypeLabelPipe
  ],
  templateUrl: './resident-report.component.html',
  styleUrl: './resident-report.component.css'
})
export class ResidentReportComponent implements OnInit {
  readonly columns = ['placa', 'tipo', 'minutos', 'importe'];
  readonly filters: { value: ReportFilter; label: string }[] = [
    { value: 'ALL', label: 'Todos los vehículos' },
    { value: 'OFFICIAL', label: 'Oficiales' },
    { value: 'RESIDENT', label: 'Residentes' },
    { value: 'NON_RESIDENT', label: 'No residentes' }
  ];

  selectedType: ReportFilter = 'ALL';
  payments: PaymentReport[] = [];
  loading = true;

  constructor(private readonly api: ParkingApiService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    const type = this.selectedType === 'ALL' ? undefined : this.selectedType;
    this.api.getPaymentReport(type).subscribe({
      next: payments => {
        this.payments = payments;
        this.loading = false;
      },
      error: () => (this.loading = false)
    });
  }

  changeType(type: ReportFilter): void {
    this.selectedType = type;
    this.load();
  }

  get total(): number {
    return this.payments.reduce((sum, payment) => sum + payment.importe, 0);
  }

  downloadCsv(): void {
    const header = ['Placa', 'Tipo', 'Minutos acumulados', 'Importe'];
    const rows = this.payments.map(payment => [
      payment.placa,
      this.typeLabel(payment.tipo),
      String(payment.minutosAcumulados),
      payment.importe.toFixed(2)
    ]);
    const csv = [header, ...rows]
      .map(row => row.map(value => this.escapeCsv(value)).join(','))
      .join('\n');
    const blob = new Blob([`\ufeff${csv}`], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');

    link.href = url;
    link.download = `reporte-pagos-${this.selectedType.toLowerCase()}.csv`;
    link.click();
    URL.revokeObjectURL(url);
  }

  private typeLabel(type: VehicleType): string {
    return {
      OFFICIAL: 'Oficial',
      RESIDENT: 'Residente',
      NON_RESIDENT: 'No residente'
    }[type];
  }

  private escapeCsv(value: string): string {
    return `"${value.replaceAll('"', '""')}"`;
  }
}
