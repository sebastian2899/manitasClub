import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INinio, Ninio } from '../ninio.model';
import { NinioService } from '../service/ninio.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAcudiente } from 'app/entities/acudiente/acudiente.model';
import { AcudienteService } from 'app/entities/acudiente/service/acudiente.service';

@Component({
  selector: 'jhi-ninio-update',
  templateUrl: './ninio-update.component.html',
})
export class NinioUpdateComponent implements OnInit {
  isSaving = false;

  acudientesCollection: IAcudiente[] = [];

  editForm = this.fb.group({
    id: [],
    nombres: [],
    apellidos: [],
    doucumentoIdentidad: [],
    fechaNacimiento: [],
    edad: [],
    observacion: [],
    descripcionObservacion: [],
    foto: [],
    fotoContentType: [],
    acudiente: [],
    idAcudiente: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ninioService: NinioService,
    protected acudienteService: AcudienteService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ninio }) => {
      if (ninio.id === undefined) {
        const today = dayjs().startOf('day');
        ninio.fechaNacimiento = today;
      }

      this.updateForm(ninio);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('manitasclubApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ninio = this.createFromForm();
    if (ninio.id !== undefined) {
      this.subscribeToSaveResponse(this.ninioService.update(ninio));
    } else {
      this.subscribeToSaveResponse(this.ninioService.create(ninio));
    }
  }

  trackAcudienteById(_index: number, item: IAcudiente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INinio>>): void {
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

  protected updateForm(ninio: INinio): void {
    this.editForm.patchValue({
      id: ninio.id,
      nombres: ninio.nombres,
      apellidos: ninio.apellidos,
      doucumentoIdentidad: ninio.doucumentoIdentidad,
      fechaNacimiento: ninio.fechaNacimiento ? ninio.fechaNacimiento.format(DATE_TIME_FORMAT) : null,
      edad: ninio.edad,
      observacion: ninio.observacion,
      descripcionObservacion: ninio.descripcionObservacion,
      foto: ninio.foto,
      fotoContentType: ninio.fotoContentType,
      acudiente: ninio.acudiente,
      idAcudiente: ninio.idAcudiente,
    });

    this.acudientesCollection = this.acudienteService.addAcudienteToCollectionIfMissing(this.acudientesCollection, ninio.acudiente);
  }

  protected loadRelationshipsOptions(): void {
    this.acudienteService.query().subscribe((res: HttpResponse<IAcudiente[]>) => (this.acudientesCollection = res.body ?? []));
  }

  protected createFromForm(): INinio {
    return {
      ...new Ninio(),
      id: this.editForm.get(['id'])!.value,
      nombres: this.editForm.get(['nombres'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      doucumentoIdentidad: this.editForm.get(['doucumentoIdentidad'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value
        ? dayjs(this.editForm.get(['fechaNacimiento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      edad: this.editForm.get(['edad'])!.value,
      observacion: this.editForm.get(['observacion'])!.value,
      descripcionObservacion: this.editForm.get(['descripcionObservacion'])!.value,
      fotoContentType: this.editForm.get(['fotoContentType'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      acudiente: this.editForm.get(['acudiente'])!.value,
      idAcudiente: this.editForm.get(['idAcudiente'])!.value,
    };
  }
}
