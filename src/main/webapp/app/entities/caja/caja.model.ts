import dayjs from 'dayjs/esm';
import { EstadoCaja } from 'app/entities/enumerations/estado-caja.model';

export interface ICaja {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  valorDia?: number | null;
  valorRegistrado?: number | null;
  diferencia?: number | null;
  estado?: EstadoCaja | null;
}

export class Caja implements ICaja {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public valorDia?: number | null,
    public valorRegistrado?: number | null,
    public diferencia?: number | null,
    public estado?: EstadoCaja | null
  ) {}
}

export function getCajaIdentifier(caja: ICaja): number | undefined {
  return caja.id;
}
