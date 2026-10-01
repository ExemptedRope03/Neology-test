export type VehicleType = 'OFFICIAL' | 'RESIDENT' | 'NON_RESIDENT';
export type VehicleStatus = 'ACTIVE' | 'INACTIVE';

export interface ApiResponse<T> {
  timestamp: string;
  status: number;
  data: T;
  succes: boolean;
}

export interface Vehicle {
  placa: string;
  tipo: VehicleType;
  status: VehicleStatus;
}

export interface Stay {
  id: number;
  fechaHoraEntrada: string;
  fechaHoraSalida: string | null;
}

export interface VehicleDetail extends Vehicle {
  estancias: Stay[];
  minutosAcumulados: number | null;
  pagoAcumulado: number | null;
}

export interface ResidentPayment {
  placa: string;
  minutosAcumulados: number;
  importe: number;
}

export interface PaymentReport {
  placa: string;
  tipo: VehicleType;
  minutosAcumulados: number;
  importe: number;
}

export interface StayResult {
  estanciaId: number;
  placa: string;
  fechaHoraEntrada?: string;
  fechaHoraSalida?: string;
  minutos?: number;
  importeACobrar?: number;
}

export interface StayOverview {
  estanciaId: number;
  placa: string;
  tipoVehiculo: VehicleType;
  fechaHoraEntrada: string;
  fechaHoraSalida: string | null;
  minutos: number;
  costo: number;
  pendiente: boolean;
}

export interface MonthResetResult {
  estanciasEliminadas: number;
  residentesReiniciados: number;
}
