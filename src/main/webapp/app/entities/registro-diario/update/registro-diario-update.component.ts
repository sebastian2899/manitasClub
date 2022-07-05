import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRegistroDiario, RegistroDiario } from '../registro-diario.model';
import { RegistroDiarioService } from '../service/registro-diario.service';

@Component({
  selector: 'jhi-registro-diario-update',
  templateUrl: './registro-diario-update.component.html',
})
export class RegistroDiarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombreNinio: [],
    nombreAcudiente: [],
    telefonoAcudiente: [],
    valor: [],
    fechaIngreso: [],
    horaEntrada: [],
    horaSalida: [],
  });

  constructor(
    protected registroDiarioService: RegistroDiarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registroDiario }) => {
      if (registroDiario.id === undefined) {
        const today = dayjs().startOf('day');
        registroDiario.fechaIngreso = today;
      }

      this.updateForm(registroDiario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registroDiario = this.createFromForm();
    if (registroDiario.id !== undefined) {
      this.subscribeToSaveResponse(this.registroDiarioService.update(registroDiario));
    } else {
      this.subscribeToSaveResponse(this.registroDiarioService.create(registroDiario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegistroDiario>>): void {
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

  protected updateForm(registroDiario: IRegistroDiario): void {
    this.editForm.patchValue({
      id: registroDiario.id,
      nombreNinio: registroDiario.nombreNinio,
      nombreAcudiente: registroDiario.nombreAcudiente,
      telefonoAcudiente: registroDiario.telefonoAcudiente,
      valor: registroDiario.valor,
      fechaIngreso: registroDiario.fechaIngreso ? registroDiario.fechaIngreso.format(DATE_TIME_FORMAT) : null,
      horaEntrada: registroDiario.horaEntrada,
      horaSalida: registroDiario.horaSalida,
    });
  }

  protected createFromForm(): IRegistroDiario {
    return {
      ...new RegistroDiario(),
      id: this.editForm.get(['id'])!.value,
      nombreNinio: this.editForm.get(['nombreNinio'])!.value,
      nombreAcudiente: this.editForm.get(['nombreAcudiente'])!.value,
      telefonoAcudiente: this.editForm.get(['telefonoAcudiente'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      fechaIngreso: this.editForm.get(['fechaIngreso'])!.value
        ? dayjs(this.editForm.get(['fechaIngreso'])!.value, DATE_TIME_FORMAT)
        : undefined,
      horaEntrada: this.editForm.get(['horaEntrada'])!.value,
      horaSalida: this.editForm.get(['horaSalida'])!.value,
    };
  }
}
