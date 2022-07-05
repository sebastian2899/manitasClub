import dayjs from 'dayjs/esm';

export interface IRegistroDiario {
  id?: number;
  nombreNinio?: string | null;
  nombreAcudiente?: string | null;
  telefonoAcudiente?: string | null;
  valor?: number | null;
  fechaIngreso?: dayjs.Dayjs | null;
  horaEntrada?: string | null;
  horaSalida?: string | null;
}

export class RegistroDiario implements IRegistroDiario {
  constructor(
    public id?: number,
    public nombreNinio?: string | null,
    public nombreAcudiente?: string | null,
    public telefonoAcudiente?: string | null,
    public valor?: number | null,
    public fechaIngreso?: dayjs.Dayjs | null,
    public horaEntrada?: string | null,
    public horaSalida?: string | null
  ) {}
}

export function getRegistroDiarioIdentifier(registroDiario: IRegistroDiario): number | undefined {
  return registroDiario.id;
}
