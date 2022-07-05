import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembresia } from '../membresia.model';
import { MembresiaService } from '../service/membresia.service';
import { MembresiaDeleteDialogComponent } from '../delete/membresia-delete-dialog.component';

@Component({
  selector: 'jhi-membresia',
  templateUrl: './membresia.component.html',
})
export class MembresiaComponent implements OnInit {
  membresias?: IMembresia[];
  isLoading = false;

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
