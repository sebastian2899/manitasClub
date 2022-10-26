import { TipoIdentificacion } from 'app/entities/enumerations/tipo-identificacion.model';

export interface IAcudiente {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  tipoIdentifiacacion?: TipoIdentificacion | null;
  direccion?: string | null;
  telefono?: string | null;
  email?: string | null;
  parentesco?: string | null;
  numeroIdentificacion?: string | null;
}

export class Acudiente implements IAcudiente {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellido?: string | null,
    public tipoIdentifiacacion?: TipoIdentificacion | null,
    public direccion?: string | null,
    public telefono?: string | null,
    public email?: string | null,
    public parentesco?: string | null,
    public numeroIdentificacion?: string | null
  ) {}
}

export function getAcudienteIdentifier(acudiente: IAcudiente): number | undefined {
  return acudiente.id;
}
