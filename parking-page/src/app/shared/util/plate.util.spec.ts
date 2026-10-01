/// <reference types="jasmine" />

import { FormControl } from '@angular/forms';
import { normalizePlate, plateValidator } from './plate.util';

describe('normalizePlate', () => {
  it('normaliza y elimina caracteres no permitidos', () => {
    expect(normalizePlate(' ab<script>-12 ')).toBe('ABSCRIPT-12');
  });

  it('rechaza caracteres no permitidos en una placa', () => {
    const control = new FormControl('AB<script>-12');

    expect(plateValidator(control)).toEqual({ invalidPlate: true });
  });

  it('acepta placas válidas sin distinguir mayúsculas y minúsculas', () => {
    const control = new FormControl('abc-123');

    expect(plateValidator(control)).toBeNull();
  });
});
