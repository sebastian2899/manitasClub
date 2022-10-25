import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMembresia, Membresia } from '../membresia.model';
import { MembresiaService } from '../service/membresia.service';
import { ITipoMembresia } from 'app/entities/tipo-membresia/tipo-membresia.model';
import { TipoMembresiaService } from 'app/entities/tipo-membresia/service/tipo-membresia.service';
import { INinio } from 'app/entities/ninio/ninio.model';
import { NinioService } from 'app/entities/ninio/service/ninio.service';
import { EstadoMembresia } from 'app/entities/enumerations/estado-membresia.model';

@Component({
  selector: 'jhi-membresia-update',
  templateUrl: './membresia-update.component.html',
})
export class MembresiaUpdateComponent implements OnInit {
  isSaving = false;
  estadoMembresiaValues = Object.keys(EstadoMembresia);

  tiposCollection: ITipoMembresia[] = [];
  niniosCollection: INinio[] = [];
  tipoMembresias: ITipoMembresia[] = [];

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    fechaInicio: [],
    fechaFin: [],
    cantidad: [],
    estado: [],
    descripcion: [],
    tipo: [],
    ninio: [],
    precioMembresia: [],
  });

  constructor(
    protected membresiaService: MembresiaService,
    protected tipoMembresiaService: TipoMembresiaService,
    protected ninioService: NinioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membresia }) => {
      if (membresia.id === undefined) {
        const today = dayjs().startOf('day');
        membresia.fechaCreacion = today;
        membresia.fechaInicio = today;
        membresia.fechaFin = today;
      }

      this.updateForm(membresia);

      this.loadRelationshipsOptions();
      this.tipoMembresiaService.query().subscribe((res: HttpResponse<ITipoMembresia[]>) => (this.tipoMembresias = res.body ?? []));
    });
  }

  calcularFechaFin(): void {
    let cantTotal;
    const tipoMembresia = this.editForm.get(['tipo'])!.value;
    const fechaInicio = this.editForm.get(['fechaInicio'])!.value;
    if (tipoMembresia) {
      const cantidad = this.editForm.get(['cantidad'])!.value;
      if (tipoMembresia.nombreMembresia === 'Mes') {
        cantTotal = cantidad * 30;
      } else if (tipoMembresia.nombreMembresia === 'Quincena') {
        cantTotal = cantidad * 15;
      } else if (tipoMembresia.nombreMembresia === 'Semana') {
        cantTotal = cantidad * 7;
      }

      this.calcularPrecioMembresia();
      const fechaFin = dayjs(fechaInicio).add(Number(cantTotal), 'day');
      this.editForm.get(['fechaFin'])!.setValue(fechaFin.format(DATE_TIME_FORMAT));
      this.editForm.get(['estado'])!.setValue(EstadoMembresia.ACTIVA);
    }
  }

  calcularPrecioMembresia(): void {
    const tipoMembresia = this.editForm.get(['tipo'])!.value;
    const cantidad = this.editForm.get(['cantidad'])!.value;
    if (tipoMembresia && cantidad) {
      this.editForm.get(['precioMembresia'])!.setValue(Number(tipoMembresia.valorMembresia) * Number(cantidad));
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const membresia = this.createFromForm();
    if (membresia.id !== undefined) {
      this.subscribeToSaveResponse(this.membresiaService.update(membresia));
    } else {
      this.subscribeToSaveResponse(this.membresiaService.create(membresia));
    }
  }

  trackTipoMembresiaById(_index: number, item: ITipoMembresia): number {
    return item.id!;
  }

  trackNinioById(_index: number, item: INinio): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMembresia>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(membresia: IMembresia): void {
    this.editForm.patchValue({
      id: membresia.id,
      fechaCreacion: membresia.fechaCreacion ? membresia.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      fechaInicio: membresia.fechaInicio ? membresia.fechaInicio.format(DATE_TIME_FORMAT) : null,
      fechaFin: membresia.fechaFin ? membresia.fechaFin.format(DATE_TIME_FORMAT) : null,
      cantidad: membresia.cantidad,
      estado: membresia.estado,
      descripcion: membresia.descripcion,
      tipo: membresia.tipo,
      ninio: membresia.ninio,
      precioMembresia: membresia.precioMembresia,
    });

    this.tiposCollection = this.tipoMembresiaService.addTipoMembresiaToCollectionIfMissing(this.tiposCollection, membresia.tipo);
    this.niniosCollection = this.ninioService.addNinioToCollectionIfMissing(this.niniosCollection, membresia.ninio);
  }

  protected loadRelationshipsOptions(): void {
    this.tipoMembresiaService
      .query({ filter: 'membresia-is-null' })
      .pipe(map((res: HttpResponse<ITipoMembresia[]>) => res.body ?? []))
      .pipe(
        map((tipoMembresias: ITipoMembresia[]) =>
          this.tipoMembresiaService.addTipoMembresiaToCollectionIfMissing(tipoMembresias, this.editForm.get('tipo')!.value)
        )
      )
      .subscribe((tipoMembresias: ITipoMembresia[]) => (this.tiposCollection = tipoMembresias));

    this.ninioService
      .query({ filter: 'membresia-is-null' })
      .pipe(map((res: HttpResponse<INinio[]>) => res.body ?? []))
      .pipe(map((ninios: INinio[]) => this.ninioService.addNinioToCollectionIfMissing(ninios, this.editForm.get('ninio')!.value)))
      .subscribe((ninios: INinio[]) => (this.niniosCollection = ninios));
  }

  protected createFromForm(): IMembresia {
    return {
      ...new Membresia(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? dayjs(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? dayjs(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      cantidad: this.editForm.get(['cantidad'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      ninio: this.editForm.get(['ninio'])!.value,
      precioMembresia: this.editForm.get(['precioMembresia'])!.value,
    };
  }
}
