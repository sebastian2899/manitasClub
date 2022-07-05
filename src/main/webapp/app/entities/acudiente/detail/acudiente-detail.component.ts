import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAcudiente } from '../acudiente.model';

@Component({
  selector: 'jhi-acudiente-detail',
  templateUrl: './acudiente-detail.component.html',
})
export class AcudienteDetailComponent implements OnInit {
  acudiente: IAcudiente | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acudiente }) => {
      this.acudiente = acudiente;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
