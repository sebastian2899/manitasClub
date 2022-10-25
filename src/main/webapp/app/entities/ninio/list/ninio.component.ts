import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INinio, Ninio } from '../ninio.model';
import { NinioService } from '../service/ninio.service';
import { NinioDeleteDialogComponent } from '../delete/ninio-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ninio',
  templateUrl: './ninio.component.html',
})
export class NinioComponent implements OnInit {
  ninios?: INinio[];
  isLoading = false;
  nombreNinio?: string | null;
  apellidosNinio?: string | null;
  isObervacion?: boolean = false;

  constructor(protected ninioService: NinioService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ninioService.query().subscribe({
      next: (res: HttpResponse<INinio[]>) => {
        this.isLoading = false;
        this.ninios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadAllFilters(): void {
    const ninio = new Ninio();
    ninio.nombres = this.nombreNinio;
    ninio.apellidos = this.apellidosNinio;
    ninio.observacion = this.isObervacion;

    this.ninioService.queryFilter(ninio).subscribe({
      next: (res: HttpResponse<INinio[]>) => {
        this.isLoading = false;
        this.ninios = res.body ?? [];
      },
    });
  }

  isObsv(): void {
    this.isObervacion = !this.isObervacion;
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: INinio): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(ninio: INinio): void {
    const modalRef = this.modalService.open(NinioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ninio = ninio;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
