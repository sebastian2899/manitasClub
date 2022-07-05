import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITotalGastos } from '../total-gastos.model';
import { TotalGastosService } from '../service/total-gastos.service';
import { TotalGastosDeleteDialogComponent } from '../delete/total-gastos-delete-dialog.component';

@Component({
  selector: 'jhi-total-gastos',
  templateUrl: './total-gastos.component.html',
})
export class TotalGastosComponent implements OnInit {
  totalGastos?: ITotalGastos[];
  isLoading = false;

  constructor(protected totalGastosService: TotalGastosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.totalGastosService.query().subscribe({
      next: (res: HttpResponse<ITotalGastos[]>) => {
        this.isLoading = false;
        this.totalGastos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITotalGastos): number {
    return item.id!;
  }

  delete(totalGastos: ITotalGastos): void {
    const modalRef = this.modalService.open(TotalGastosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.totalGastos = totalGastos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
