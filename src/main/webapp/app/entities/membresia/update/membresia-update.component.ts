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
  tipSeleccionado!: ITipoMembresia | null;
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
    idNinio: [],
    idTipo: [],
    valorPagado: [],
    deuda: [],
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
    const tipoMembresia = this.editForm.get(['idTipo'])!.value;

    this.tiposCollection.forEach(element => {
      if (element.id === tipoMembresia) {
        this.tipSeleccionado = element;
      }
    });

    const fechaInicio = this.editForm.get(['fechaInicio'])!.value;
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (this.tipSeleccionado && this.tipSeleccionado.nombreMembresia!) {
      const cantidad = this.editForm.get(['cantidad'])!.value;
      if (this.tipSeleccionado.nombreMembresia === 'Mes') {
        cantTotal = cantidad * 30;
      } else if (this.tipSeleccionado.nombreMembresia === 'Quincena') {
        cantTotal = cantidad * 15;
      } else if (this.tipSeleccionado.nombreMembresia === 'Semana') {
        cantTotal = cantidad * 7;
      }

      this.calcularPrecioMembresia();
      const fechaFin = dayjs(fechaInicio).add(Number(cantTotal), 'day');
      this.editForm.get(['fechaFin'])!.setValue(fechaFin.format(DATE_TIME_FORMAT));
      this.editForm.get(['estado'])!.setValue(EstadoMembresia.ACTIVA);
    }
  }

  calcularPrecioMembresia(): void {
    const tipoMembre = this.editForm.get(['idTipo'])!.value;

    this.tiposCollection.forEach(element => {
      if (element.id === tipoMembre) {
        this.tipSeleccionado = element;
      }
    });

    const cantidad = this.editForm.get(['cantidad'])!.value;
    if (cantidad) {
      this.editForm.get(['precioMembresia'])!.setValue(Number(this.tipSeleccionado!.valorMembresia) * Number(cantidad));
    }
  }

  calcularDeuda(): void {
    const valorPagado = this.editForm.get(['valorPagado'])!.value;
    const precioMembresia = this.editForm.get(['precioMembresia'])!.value;

    this.editForm.get(['deuda'])!.setValue(Number(precioMembresia) - Number(valorPagado));

    this.editForm.get(['deuda'])!.valueChanges.subscribe(res => {
      res === 0
        ? this.editForm.get(['estado'])!.setValue(EstadoMembresia.ACTIVA)
        : this.editForm.get(['estado'])!.setValue(EstadoMembresia.DEUDA);
    });
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
      idNinio: membresia.idNinio,
      idTipo: membresia.idTipo,
      valorPagado: membresia.valorPagado,
      deuda: membresia.deuda,
    });

    this.tiposCollection = this.tipoMembresiaService.addTipoMembresiaToCollectionIfMissing(this.tiposCollection, membresia.tipo);
    this.niniosCollection = this.ninioService.addNinioToCollectionIfMissing(this.niniosCollection, membresia.ninio);
  }

  protected loadRelationshipsOptions(): void {
    this.tipoMembresiaService
      .query({ filter: 'membresia-is-null' })
      .subscribe((res: HttpResponse<ITipoMembresia[]>) => (this.tiposCollection = res.body ?? []));

    this.ninioService
      .query({ filter: 'membresia-is-null' })
      .subscribe((res: HttpResponse<INinio[]>) => (this.niniosCollection = res.body ?? []));
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
      idNinio: this.editForm.get(['idNinio'])!.value,
      idTipo: this.editForm.get(['idTipo'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      deuda: this.editForm.get(['deuda'])!.value,
    };
  }
}
