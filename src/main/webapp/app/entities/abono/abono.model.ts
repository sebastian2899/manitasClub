import dayjs from 'dayjs/esm';

export interface IAbono {
  id?: number;
  idMembresia?: number | null;
  fechaAbono?: dayjs.Dayjs | null;
  valorAbono?: number | null;
}

export class Abono implements IAbono {
  constructor(
    public id?: number,
    public idMembresia?: number | null,
    public fechaAbono?: dayjs.Dayjs | null,
    public valorAbono?: number | null
  ) {}
}

export function getAbonoIdentifier(abono: IAbono): number | undefined {
  return abono.id;
}
