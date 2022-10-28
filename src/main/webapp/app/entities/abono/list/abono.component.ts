import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbono } from '../abono.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AbonoService } from '../service/abono.service';
import { AbonoDeleteDialogComponent } from '../delete/abono-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { MembresiaService } from 'app/entities/membresia/service/membresia.service';
import { IMembresia } from 'app/entities/membresia/membresia.model';

@Component({
  selector: 'jhi-abono',
  templateUrl: './abono.component.html',
})
export class AbonoComponent implements OnInit {
  abonos: IAbono[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  membresia?: IMembresia;

  constructor(
    protected abonoService: AbonoService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private membresiaServioce: MembresiaService
  ) {
    this.abonos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.abonoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IAbono[]>) => {
          this.isLoading = false;
          this.paginateAbonos(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.abonos = [];
    this.abonosMembresia(this.membresia!.id!);
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.membresia = this.membresiaServioce.instaciaMembresia;
    this.abonosMembresia(this.membresia.id!);
  }

  abonosMembresia(id: number): void {
    this.isLoading = true;
    this.abonoService.getAbonosMembresia(id).subscribe({
      next: (res: HttpResponse<IAbono[]>) => {
        this.isLoading = false;
        this.paginateAbonos(res.body, res.headers);
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  back(): void {
    window.history.back();
  }

  trackId(_index: number, item: IAbono): number {
    return item.id!;
  }

  delete(abono: IAbono): void {
    const modalRef = this.modalService.open(AbonoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.abono = abono;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAbonos(data: IAbono[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.abonos.push(d);
      }
    }
  }
}
