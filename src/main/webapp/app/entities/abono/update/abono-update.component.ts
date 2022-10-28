import { MembresiaService } from './../../membresia/service/membresia.service';
import { IMembresia } from './../../membresia/membresia.model';
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAbono, Abono } from '../abono.model';
import { AbonoService } from '../service/abono.service';

@Component({
  selector: 'jhi-abono-update',
  templateUrl: './abono-update.component.html',
})
export class AbonoUpdateComponent implements OnInit {
  isSaving = false;
  membresia?: IMembresia | null;
  editForm = this.fb.group({
    id: [],
    idMembresia: [],
    fechaAbono: [],
    valorAbono: [],
  });

  constructor(
    protected abonoService: AbonoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private membresiaService: MembresiaService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abono }) => {
      if (abono.id === undefined) {
        const today = dayjs().startOf('day');
        abono.fechaAbono = today;
      }

      this.updateForm(abono);
    });
    this.membresia = this.membresiaService.instaciaMembresia;
    this.editForm.get(['idMembresia'])!.setValue(this.membresia.id);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const abono = this.createFromForm();
    if (abono.id !== undefined) {
      this.subscribeToSaveResponse(this.abonoService.update(abono));
    } else {
      this.subscribeToSaveResponse(this.abonoService.create(abono));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbono>>): void {
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

  protected updateForm(abono: IAbono): void {
    this.editForm.patchValue({
      id: abono.id,
      idMembresia: abono.idMembresia,
      fechaAbono: abono.fechaAbono ? abono.fechaAbono.format(DATE_TIME_FORMAT) : null,
      valorAbono: abono.valorAbono,
    });
  }

  protected createFromForm(): IAbono {
    return {
      ...new Abono(),
      id: this.editForm.get(['id'])!.value,
      idMembresia: this.membresia!.id,
      fechaAbono: this.editForm.get(['fechaAbono'])!.value ? dayjs(this.editForm.get(['fechaAbono'])!.value, DATE_TIME_FORMAT) : undefined,
      valorAbono: this.editForm.get(['valorAbono'])!.value,
    };
  }
}
