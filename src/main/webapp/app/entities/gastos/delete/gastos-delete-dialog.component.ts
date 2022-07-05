import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGastos } from '../gastos.model';
import { GastosService } from '../service/gastos.service';

@Component({
  templateUrl: './gastos-delete-dialog.component.html',
})
export class GastosDeleteDialogComponent {
  gastos?: IGastos;

  constructor(protected gastosService: GastosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gastosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
