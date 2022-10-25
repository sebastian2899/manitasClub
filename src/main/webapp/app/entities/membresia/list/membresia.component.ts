import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembresia } from '../membresia.model';
import { MembresiaService } from '../service/membresia.service';
import { MembresiaDeleteDialogComponent } from '../delete/membresia-delete-dialog.component';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-membresia',
  templateUrl: './membresia.component.html',
})
export class MembresiaComponent implements OnInit {
  membresias?: IMembresia[];
  isLoading = false;
  @ViewChild('consultarCajaFechas', { static: true }) content: ElementRef | undefined;
  fechaInicio?: dayjs.Dayjs;
  fechaFin?: dayjs.Dayjs;
  valorTotal = 0;

  constructor(protected membresiaService: MembresiaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.membresiaService.query().subscribe({
      next: (res: HttpResponse<IMembresia[]>) => {
        this.isLoading = false;
        this.membresias = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  consultarValorMeses(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.membresiaService.consultarValorMes(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe({
        next: (res: HttpResponse<number>) => {
          this.valorTotal = res.body ?? 0;
        },
        error: () => {
          this.valorTotal = 0;
        },
      });
    }
  }

  opneModal(): void {
    this.modalService.open(this.content, { size: 'lg', backdrop: 'static', centered: true });
  }

  back(): void {
    this.modalService.dismissAll();
    this.valorTotal = 0;
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMembresia): number {
    return item.id!;
  }

  delete(membresia: IMembresia): void {
    const modalRef = this.modalService.open(MembresiaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.membresia = membresia;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
