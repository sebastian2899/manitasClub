import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGastos } from '../gastos.model';
import { GastosService } from '../service/gastos.service';
import { GastosDeleteDialogComponent } from '../delete/gastos-delete-dialog.component';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-gastos',
  templateUrl: './gastos.component.html',
})
export class GastosComponent implements OnInit {
  gastos?: IGastos[];
  isLoading = false;
  @ViewChild('consultarCajaFechas', { static: true }) content: ElementRef | undefined;
  fechaInicio?: dayjs.Dayjs;
  fechaFin?: dayjs.Dayjs;
  valorTotal = 0;

  constructor(protected gastosService: GastosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.gastosService.query().subscribe({
      next: (res: HttpResponse<IGastos[]>) => {
        this.isLoading = false;
        this.gastos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  consultarValorMeses(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.gastosService.consultarValorMes(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe({
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

  trackId(_index: number, item: IGastos): number {
    return item.id!;
  }

  delete(gastos: IGastos): void {
    const modalRef = this.modalService.open(GastosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.gastos = gastos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
