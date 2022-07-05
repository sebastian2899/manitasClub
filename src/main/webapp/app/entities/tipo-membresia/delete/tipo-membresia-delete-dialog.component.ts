import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoMembresia } from '../tipo-membresia.model';
import { TipoMembresiaService } from '../service/tipo-membresia.service';

@Component({
  templateUrl: './tipo-membresia-delete-dialog.component.html',
})
export class TipoMembresiaDeleteDialogComponent {
  tipoMembresia?: ITipoMembresia;

  constructor(protected tipoMembresiaService: TipoMembresiaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoMembresiaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
