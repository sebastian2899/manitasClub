import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegistroDiario } from '../registro-diario.model';
import { RegistroDiarioService } from '../service/registro-diario.service';
import { RegistroDiarioDeleteDialogComponent } from '../delete/registro-diario-delete-dialog.component';

@Component({
  selector: 'jhi-registro-diario',
  templateUrl: './registro-diario.component.html',
})
export class RegistroDiarioComponent implements OnInit {
  registroDiarios?: IRegistroDiario[];
  isLoading = false;

  constructor(protected registroDiarioService: RegistroDiarioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.registroDiarioService.query().subscribe({
      next: (res: HttpResponse<IRegistroDiario[]>) => {
        this.isLoading = false;
        this.registroDiarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRegistroDiario): number {
    return item.id!;
  }

  delete(registroDiario: IRegistroDiario): void {
    const modalRef = this.modalService.open(RegistroDiarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.registroDiario = registroDiario;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
