import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegistroDiario } from '../registro-diario.model';

@Component({
  selector: 'jhi-registro-diario-detail',
  templateUrl: './registro-diario-detail.component.html',
})
export class RegistroDiarioDetailComponent implements OnInit {
  registroDiario: IRegistroDiario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registroDiario }) => {
      this.registroDiario = registroDiario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
