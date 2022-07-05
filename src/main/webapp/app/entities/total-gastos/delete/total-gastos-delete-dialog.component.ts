import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITotalGastos } from '../total-gastos.model';
import { TotalGastosService } from '../service/total-gastos.service';

@Component({
  templateUrl: './total-gastos-delete-dialog.component.html',
})
export class TotalGastosDeleteDialogComponent {
  totalGastos?: ITotalGastos;

  constructor(protected totalGastosService: TotalGastosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.totalGastosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
