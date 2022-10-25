import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaja } from '../caja.model';
import { CajaService } from '../service/caja.service';
import { CajaDeleteDialogComponent } from '../delete/caja-delete-dialog.component';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-caja',
  templateUrl: './caja.component.html',
})
export class CajaComponent implements OnInit {
  @ViewChild('consultarCajaFechas', { static: true }) content: ElementRef | undefined;
  cajas?: ICaja[];
  isLoading = false;
  fechaInicio?: dayjs.Dayjs;
  fechaFin?: dayjs.Dayjs;
  valorTotal = 0;

  constructor(protected cajaService: CajaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cajaService.query().subscribe({
      next: (res: HttpResponse<ICaja[]>) => {
        this.isLoading = false;
        this.cajas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  consultarValorMeses(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.cajaService.consultarValorMes(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe({
        next: (res: HttpResponse<number>) => {
          this.valorTotal = res.body ?? 0;
        },
        error: () => {
          this.valorTotal = 0;
        },
      });
    }
  }
  trackId(_index: number, item: ICaja): number {
    return item.id!;
  }

  opneModal(): void {
    this.modalService.open(this.content, { size: 'lg', backdrop: 'static', centered: true });
  }

  back(): void {
    this.modalService.dismissAll();
    this.valorTotal = 0;
  }

  delete(caja: ICaja): void {
    const modalRef = this.modalService.open(CajaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.caja = caja;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
