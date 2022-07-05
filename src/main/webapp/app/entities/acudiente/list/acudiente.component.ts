import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAcudiente } from '../acudiente.model';
import { AcudienteService } from '../service/acudiente.service';
import { AcudienteDeleteDialogComponent } from '../delete/acudiente-delete-dialog.component';

@Component({
  selector: 'jhi-acudiente',
  templateUrl: './acudiente.component.html',
})
export class AcudienteComponent implements OnInit {
  acudientes?: IAcudiente[];
  isLoading = false;

  constructor(protected acudienteService: AcudienteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.acudienteService.query().subscribe({
      next: (res: HttpResponse<IAcudiente[]>) => {
        this.isLoading = false;
        this.acudientes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAcudiente): number {
    return item.id!;
  }

  delete(acudiente: IAcudiente): void {
    const modalRef = this.modalService.open(AcudienteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.acudiente = acudiente;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
