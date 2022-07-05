import dayjs from 'dayjs/esm';
import { EstadoCaja } from 'app/entities/enumerations/estado-caja.model';

export interface ITotalGastos {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  valorInicial?: number | null;
  valorTotalGastos?: number | null;
  diferencia?: number | null;
  estado?: EstadoCaja | null;
}

export class TotalGastos implements ITotalGastos {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public valorInicial?: number | null,
    public valorTotalGastos?: number | null,
    public diferencia?: number | null,
    public estado?: EstadoCaja | null
  ) {}
}

export function getTotalGastosIdentifier(totalGastos: ITotalGastos): number | undefined {
  return totalGastos.id;
}
