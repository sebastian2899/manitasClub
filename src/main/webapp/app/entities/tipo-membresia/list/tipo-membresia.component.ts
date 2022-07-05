import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoMembresia } from '../tipo-membresia.model';
import { TipoMembresiaService } from '../service/tipo-membresia.service';
import { TipoMembresiaDeleteDialogComponent } from '../delete/tipo-membresia-delete-dialog.component';

@Component({
  selector: 'jhi-tipo-membresia',
  templateUrl: './tipo-membresia.component.html',
})
export class TipoMembresiaComponent implements OnInit {
  tipoMembresias?: ITipoMembresia[];
  isLoading = false;

  constructor(protected tipoMembresiaService: TipoMembresiaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tipoMembresiaService.query().subscribe({
      next: (res: HttpResponse<ITipoMembresia[]>) => {
        this.isLoading = false;
        this.tipoMembresias = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITipoMembresia): number {
    return item.id!;
  }

  delete(tipoMembresia: ITipoMembresia): void {
    const modalRef = this.modalService.open(TipoMembresiaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoMembresia = tipoMembresia;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
