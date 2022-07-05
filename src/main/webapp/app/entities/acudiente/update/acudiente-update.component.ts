import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAcudiente, Acudiente } from '../acudiente.model';
import { AcudienteService } from '../service/acudiente.service';
import { TipoIdentificacion } from 'app/entities/enumerations/tipo-identificacion.model';

@Component({
  selector: 'jhi-acudiente-update',
  templateUrl: './acudiente-update.component.html',
})
export class AcudienteUpdateComponent implements OnInit {
  isSaving = false;
  tipoIdentificacionValues = Object.keys(TipoIdentificacion);

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellido: [],
    tipoIdentifiacacion: [],
    direccion: [],
    telefono: [],
    email: [],
    parentesco: [],
  });

  constructor(protected acudienteService: AcudienteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acudiente }) => {
      this.updateForm(acudiente);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const acudiente = this.createFromForm();
    if (acudiente.id !== undefined) {
      this.subscribeToSaveResponse(this.acudienteService.update(acudiente));
    } else {
      this.subscribeToSaveResponse(this.acudienteService.create(acudiente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAcudiente>>): void {
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

  protected updateForm(acudiente: IAcudiente): void {
    this.editForm.patchValue({
      id: acudiente.id,
      nombre: acudiente.nombre,
      apellido: acudiente.apellido,
      tipoIdentifiacacion: acudiente.tipoIdentifiacacion,
      direccion: acudiente.direccion,
      telefono: acudiente.telefono,
      email: acudiente.email,
      parentesco: acudiente.parentesco,
    });
  }

  protected createFromForm(): IAcudiente {
    return {
      ...new Acudiente(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      tipoIdentifiacacion: this.editForm.get(['tipoIdentifiacacion'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      email: this.editForm.get(['email'])!.value,
      parentesco: this.editForm.get(['parentesco'])!.value,
    };
  }
}
