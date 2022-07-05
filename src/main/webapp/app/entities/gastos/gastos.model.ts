import dayjs from 'dayjs/esm';

export interface IGastos {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  valor?: number | null;
  descripcion?: string | null;
}

export class Gastos implements IGastos {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public valor?: number | null,
    public descripcion?: string | null
  ) {}
}

export function getGastosIdentifier(gastos: IGastos): number | undefined {
  return gastos.id;
}
