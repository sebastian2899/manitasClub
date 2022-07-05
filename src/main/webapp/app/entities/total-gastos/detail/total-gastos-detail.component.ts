import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITotalGastos } from '../total-gastos.model';

@Component({
  selector: 'jhi-total-gastos-detail',
  templateUrl: './total-gastos-detail.component.html',
})
export class TotalGastosDetailComponent implements OnInit {
  totalGastos: ITotalGastos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ totalGastos }) => {
      this.totalGastos = totalGastos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
