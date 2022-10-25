import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegistroDiario } from '../registro-diario.model';
import { RegistroDiarioService } from '../service/registro-diario.service';
import { RegistroDiarioDeleteDialogComponent } from '../delete/registro-diario-delete-dialog.component';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-registro-diario',
  templateUrl: './registro-diario.component.html',
})
export class RegistroDiarioComponent implements OnInit {
  @ViewChild('consultarCajaFechas', { static: true }) content: ElementRef | undefined;
  registroDiarios?: IRegistroDiario[];
  isLoading = false;
  fechaInicio?: dayjs.Dayjs;
  fechaFin?: dayjs.Dayjs;
  valorTotal = 0;

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

  consultarValorMeses(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.registroDiarioService.consultarValorMes(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe({
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
