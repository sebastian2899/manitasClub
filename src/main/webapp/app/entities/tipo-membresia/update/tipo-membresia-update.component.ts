import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoMembresia, TipoMembresia } from '../tipo-membresia.model';
import { TipoMembresiaService } from '../service/tipo-membresia.service';

@Component({
  selector: 'jhi-tipo-membresia-update',
  templateUrl: './tipo-membresia-update.component.html',
})
export class TipoMembresiaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombreMembresia: [],
    valorMembresia: [],
    descripcion: [],
  });

  constructor(protected tipoMembresiaService: TipoMembresiaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoMembresia }) => {
      this.updateForm(tipoMembresia);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoMembresia = this.createFromForm();
    if (tipoMembresia.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoMembresiaService.update(tipoMembresia));
    } else {
      this.subscribeToSaveResponse(this.tipoMembresiaService.create(tipoMembresia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoMembresia>>): void {
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

  protected updateForm(tipoMembresia: ITipoMembresia): void {
    this.editForm.patchValue({
      id: tipoMembresia.id,
      nombreMembresia: tipoMembresia.nombreMembresia,
      valorMembresia: tipoMembresia.valorMembresia,
      descripcion: tipoMembresia.descripcion,
    });
  }

  protected createFromForm(): ITipoMembresia {
    return {
      ...new TipoMembresia(),
      id: this.editForm.get(['id'])!.value,
      nombreMembresia: this.editForm.get(['nombreMembresia'])!.value,
      valorMembresia: this.editForm.get(['valorMembresia'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
    };
  }
}
