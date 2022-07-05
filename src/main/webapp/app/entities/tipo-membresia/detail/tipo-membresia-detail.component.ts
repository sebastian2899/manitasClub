import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoMembresia } from '../tipo-membresia.model';

@Component({
  selector: 'jhi-tipo-membresia-detail',
  templateUrl: './tipo-membresia-detail.component.html',
})
export class TipoMembresiaDetailComponent implements OnInit {
  tipoMembresia: ITipoMembresia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoMembresia }) => {
      this.tipoMembresia = tipoMembresia;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
