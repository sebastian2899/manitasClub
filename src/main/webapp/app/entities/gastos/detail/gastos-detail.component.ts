import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGastos } from '../gastos.model';

@Component({
  selector: 'jhi-gastos-detail',
  templateUrl: './gastos-detail.component.html',
})
export class GastosDetailComponent implements OnInit {
  gastos: IGastos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gastos }) => {
      this.gastos = gastos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
