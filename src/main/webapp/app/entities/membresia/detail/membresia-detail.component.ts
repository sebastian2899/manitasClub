import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMembresia } from '../membresia.model';

@Component({
  selector: 'jhi-membresia-detail',
  templateUrl: './membresia-detail.component.html',
})
export class MembresiaDetailComponent implements OnInit {
  membresia: IMembresia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membresia }) => {
      this.membresia = membresia;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
