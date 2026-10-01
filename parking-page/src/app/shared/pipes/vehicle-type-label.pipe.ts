import { Pipe, PipeTransform } from '@angular/core';
import { VehicleType } from '../../core/models/vehicle.model';

@Pipe({ name: 'vehicleTypeLabel', standalone: true })
export class VehicleTypeLabelPipe implements PipeTransform {
  transform(type: VehicleType): string {
    return { OFFICIAL: 'Oficial', RESIDENT: 'Residente', NON_RESIDENT: 'No residente' }[type];
  }
}
