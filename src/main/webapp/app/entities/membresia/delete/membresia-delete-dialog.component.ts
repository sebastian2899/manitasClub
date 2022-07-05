import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembresia } from '../membresia.model';
import { MembresiaService } from '../service/membresia.service';

@Component({
  templateUrl: './membresia-delete-dialog.component.html',
})
export class MembresiaDeleteDialogComponent {
  membresia?: IMembresia;

  constructor(protected membresiaService: MembresiaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.membresiaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
