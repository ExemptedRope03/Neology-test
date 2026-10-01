import { AbstractControl, ValidationErrors } from '@angular/forms';

const PLATE_PATTERN = /^[A-Z0-9-]{3,20}$/;

/** Normaliza la placa y elimina caracteres que no debe recibir la API. */
export function normalizePlate(value: string): string {
  return value.trim().toUpperCase().replace(/[^A-Z0-9-]/g, '');
}

export function plateValidator(control: AbstractControl): ValidationErrors | null {
  const value = String(control.value ?? '').trim().toUpperCase();
  return PLATE_PATTERN.test(value) ? null : { invalidPlate: true };
}
