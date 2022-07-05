import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITotalGastos, TotalGastos } from '../total-gastos.model';
import { TotalGastosService } from '../service/total-gastos.service';
import { EstadoCaja } from 'app/entities/enumerations/estado-caja.model';

@Component({
  selector: 'jhi-total-gastos-update',
  templateUrl: './total-gastos-update.component.html',
})
export class TotalGastosUpdateComponent implements OnInit {
  isSaving = false;
  estadoCajaValues = Object.keys(EstadoCaja);

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    valorInicial: [],
    valorTotalGastos: [],
    diferencia: [],
    estado: [],
  });

  constructor(protected totalGastosService: TotalGastosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ totalGastos }) => {
      if (totalGastos.id === undefined) {
        const today = dayjs().startOf('day');
        totalGastos.fechaCreacion = today;
      }

      this.updateForm(totalGastos);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const totalGastos = this.createFromForm();
    if (totalGastos.id !== undefined) {
      this.subscribeToSaveResponse(this.totalGastosService.update(totalGastos));
    } else {
      this.subscribeToSaveResponse(this.totalGastosService.create(totalGastos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITotalGastos>>): void {
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

  protected updateForm(totalGastos: ITotalGastos): void {
    this.editForm.patchValue({
      id: totalGastos.id,
      fechaCreacion: totalGastos.fechaCreacion ? totalGastos.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      valorInicial: totalGastos.valorInicial,
      valorTotalGastos: totalGastos.valorTotalGastos,
      diferencia: totalGastos.diferencia,
      estado: totalGastos.estado,
    });
  }

  protected createFromForm(): ITotalGastos {
    return {
      ...new TotalGastos(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      valorInicial: this.editForm.get(['valorInicial'])!.value,
      valorTotalGastos: this.editForm.get(['valorTotalGastos'])!.value,
      diferencia: this.editForm.get(['diferencia'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
