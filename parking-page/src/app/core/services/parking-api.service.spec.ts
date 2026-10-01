/// <reference types="jasmine" />

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ParkingApiService } from './parking-api.service';

describe('ParkingApiService', () => {
  let service: ParkingApiService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(ParkingApiService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTesting.verify());

  it('obtiene y desenvuelve el listado de vehículos', () => {
    let resultLength = 0;
    service.getVehicles().subscribe(vehicles => (resultLength = vehicles.length));

    const request = httpTesting.expectOne('http://localhost:8080/neo/vehiculos');
    expect(request.request.method).toBe('GET');
    request.flush({
      timestamp: '2026-01-01T00:00:00Z',
      status: 200,
      succes: true,
      data: [{ placa: 'ABC-123', tipo: 'RESIDENT', status: 'ACTIVE' }]
    });

    expect(resultLength).toBe(1);
  });

  it('envía el PATCH de cambio de estado', () => {
    service.changeVehicleStatus('ABC-123', 'INACTIVE').subscribe();

    const request = httpTesting.expectOne('http://localhost:8080/neo/vehiculos/ABC-123');
    expect(request.request.method).toBe('PATCH');
    expect(request.request.body).toEqual({ status: 'INACTIVE' });
    request.flush({
      timestamp: '2026-01-01T00:00:00Z',
      status: 200,
      succes: true,
      data: { placa: 'ABC-123', tipo: 'RESIDENT', status: 'INACTIVE' }
    });
  });
});
