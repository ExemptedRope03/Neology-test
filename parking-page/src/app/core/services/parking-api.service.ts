import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ApiResponse,
  MonthResetResult,
  PaymentReport,
  ResidentPayment,
  StayOverview,
  StayResult,
  Vehicle,
  VehicleDetail,
  VehicleStatus,
  VehicleType
} from '../models/vehicle.model';

@Injectable({ providedIn: 'root' })
export class ParkingApiService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private readonly http: HttpClient) {}

  getVehicles(): Observable<Vehicle[]> {
    return this.http
      .get<ApiResponse<Vehicle[]>>(`${this.baseUrl}/vehiculos`)
      .pipe(map(response => response.data));
  }

  getVehicle(plate: string): Observable<VehicleDetail> {
    return this.http
      .get<ApiResponse<VehicleDetail>>(
        `${this.baseUrl}/vehiculos/${encodeURIComponent(plate)}`
      )
      .pipe(map(response => response.data));
  }

  createVehicle(plate: string, type: VehicleType): Observable<Vehicle> {
    const route: Record<VehicleType, string> = {
      OFFICIAL: 'oficiales',
      RESIDENT: 'residentes',
      NON_RESIDENT: 'no-residentes'
    };
    return this.http
      .post<ApiResponse<Vehicle>>(`${this.baseUrl}/vehiculos/${route[type]}`, {
        placa: plate
      })
      .pipe(map(response => response.data));
  }

  changeVehicleStatus(plate: string, status: VehicleStatus): Observable<Vehicle> {
    return this.http
      .patch<ApiResponse<Vehicle>>(
        `${this.baseUrl}/vehiculos/${encodeURIComponent(plate)}`,
        { status }
      )
      .pipe(map(response => response.data));
  }

  registerEntry(plate: string): Observable<StayResult> {
    return this.http
      .post<ApiResponse<StayResult>>(`${this.baseUrl}/estancias/entrada`, { placa: plate })
      .pipe(map(response => response.data));
  }

  registerExit(plate: string): Observable<StayResult> {
    return this.http
      .post<ApiResponse<StayResult>>(`${this.baseUrl}/estancias/salida`, { placa: plate })
      .pipe(map(response => response.data));
  }

  getStays(): Observable<StayOverview[]> {
    return this.http
      .get<ApiResponse<StayOverview[]>>(`${this.baseUrl}/estancias`)
      .pipe(map(response => response.data));
  }

  getResidentPayments(): Observable<ResidentPayment[]> {
    return this.http
      .get<ApiResponse<ResidentPayment[]>>(`${this.baseUrl}/residentes/pagos`)
      .pipe(map(response => response.data));
  }

  getPaymentReport(type?: VehicleType): Observable<PaymentReport[]> {
    const params = type ? new HttpParams().set('tipo', type) : undefined;
    return this.http
      .get<ApiResponse<PaymentReport[]>>(`${this.baseUrl}/reportes/pagos`, { params })
      .pipe(map(response => response.data));
  }

  startMonth(): Observable<MonthResetResult> {
    return this.http
      .post<ApiResponse<MonthResetResult>>(`${this.baseUrl}/mes/iniciar`, {})
      .pipe(map(response => response.data));
  }
}
