import dayjs from 'dayjs/esm';
import { IAcudiente } from 'app/entities/acudiente/acudiente.model';

export interface INinio {
  id?: number;
  nombres?: string | null;
  apellidos?: string | null;
  doucumentoIdentidad?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  edad?: number | null;
  observacion?: boolean | null;
  descripcionObservacion?: string | null;
  fotoContentType?: string | null;
  foto?: string | null;
  acudiente?: IAcudiente | null;
  idAcudiente?: number | null;
  acudiente2?: IAcudiente | null;
}

export class Ninio implements INinio {
  constructor(
    public id?: number,
    public nombres?: string | null,
    public apellidos?: string | null,
    public doucumentoIdentidad?: string | null,
    public fechaNacimiento?: dayjs.Dayjs | null,
    public edad?: number | null,
    public observacion?: boolean | null,
    public descripcionObservacion?: string | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public acudiente?: IAcudiente | null,
    public idAcudiente?: number | null,
    public acudiente2?: IAcudiente | null
  ) {
    this.observacion = this.observacion ?? false;
  }
}

export function getNinioIdentifier(ninio: INinio): number | undefined {
  return ninio.id;
}
