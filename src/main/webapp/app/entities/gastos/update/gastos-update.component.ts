import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IGastos, Gastos } from '../gastos.model';
import { GastosService } from '../service/gastos.service';

@Component({
  selector: 'jhi-gastos-update',
  templateUrl: './gastos-update.component.html',
})
export class GastosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    valor: [],
    descripcion: [],
  });

  constructor(protected gastosService: GastosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gastos }) => {
      if (gastos.id === undefined) {
        const today = dayjs().startOf('day');
        gastos.fechaCreacion = today;
      }

      this.updateForm(gastos);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gastos = this.createFromForm();
    if (gastos.id !== undefined) {
      this.subscribeToSaveResponse(this.gastosService.update(gastos));
    } else {
      this.subscribeToSaveResponse(this.gastosService.create(gastos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGastos>>): void {
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

  protected updateForm(gastos: IGastos): void {
    this.editForm.patchValue({
      id: gastos.id,
      fechaCreacion: gastos.fechaCreacion ? gastos.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      valor: gastos.valor,
      descripcion: gastos.descripcion,
    });
  }

  protected createFromForm(): IGastos {
    return {
      ...new Gastos(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      valor: this.editForm.get(['valor'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
    };
  }
}
