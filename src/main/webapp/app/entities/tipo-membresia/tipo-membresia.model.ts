export interface ITipoMembresia {
  id?: number;
  nombreMembresia?: string | null;
  valorMembresia?: number | null;
  descripcion?: string | null;
}

export class TipoMembresia implements ITipoMembresia {
  constructor(
    public id?: number,
    public nombreMembresia?: string | null,
    public valorMembresia?: number | null,
    public descripcion?: string | null
  ) {}
}

export function getTipoMembresiaIdentifier(tipoMembresia: ITipoMembresia): number | undefined {
  return tipoMembresia.id;
}
