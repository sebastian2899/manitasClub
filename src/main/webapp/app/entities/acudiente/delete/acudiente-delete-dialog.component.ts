import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAcudiente } from '../acudiente.model';
import { AcudienteService } from '../service/acudiente.service';

@Component({
  templateUrl: './acudiente-delete-dialog.component.html',
})
export class AcudienteDeleteDialogComponent {
  acudiente?: IAcudiente;

  constructor(protected acudienteService: AcudienteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.acudienteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
