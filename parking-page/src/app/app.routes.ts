import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { ResidentReportComponent } from './features/resident-report/resident-report.component';
import { SettingsComponent } from './features/settings/settings.component';
import { StayManagementComponent } from './features/stay-management/stay-management.component';
import { VehicleDetailComponent } from './features/vehicle-detail/vehicle-detail.component';
import { VehicleListComponent } from './features/vehicle-list/vehicle-list.component';

export const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    title: 'Panel | Parking Neology'
  },
  {
    path: 'vehiculos',
    component: VehicleListComponent,
    title: 'Vehículos | Parking Neology'
  },
  {
    path: 'vehiculos/:placa',
    component: VehicleDetailComponent,
    title: 'Detalle | Parking Neology'
  },
  {
    path: 'estancias',
    component: StayManagementComponent,
    title: 'Estancias | Parking Neology'
  },
  {
    path: 'reportes-pagos',
    component: ResidentReportComponent,
    title: 'Reporte de pagos | Parking Neology'
  },
  { path: 'residentes', redirectTo: 'reportes-pagos', pathMatch: 'full' },
  {
    path: 'configuracion',
    component: SettingsComponent,
    title: 'Configuración | Parking Neology'
  },
  { path: '**', redirectTo: '' }
];
