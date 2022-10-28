import dayjs from 'dayjs/esm';
import { ITipoMembresia } from 'app/entities/tipo-membresia/tipo-membresia.model';
import { INinio } from 'app/entities/ninio/ninio.model';
import { EstadoMembresia } from 'app/entities/enumerations/estado-membresia.model';

export interface IMembresia {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  cantidad?: number | null;
  estado?: EstadoMembresia | null;
  descripcion?: string | null;
  tipo?: ITipoMembresia | null;
  ninio?: INinio | null;
  precioMembresia?: number | null;
  idTipo?: number | null;
  idNinio?: number | null;
  deuda?: number | null;
  valorPagado?: number | null;
}

export class Membresia implements IMembresia {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaFin?: dayjs.Dayjs | null,
    public cantidad?: number | null,
    public estado?: EstadoMembresia | null,
    public descripcion?: string | null,
    public tipo?: ITipoMembresia | null,
    public ninio?: INinio | null,
    public precioMembresia?: number | null,
    public idTipo?: number | null,
    public idNinio?: number | null,
    public deuda?: number | null,
    public valorPagado?: number | null
  ) {}
}

export function getMembresiaIdentifier(membresia: IMembresia): number | undefined {
  return membresia.id;
}
