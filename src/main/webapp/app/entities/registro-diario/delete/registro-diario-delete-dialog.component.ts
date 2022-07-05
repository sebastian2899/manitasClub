import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegistroDiario } from '../registro-diario.model';
import { RegistroDiarioService } from '../service/registro-diario.service';

@Component({
  templateUrl: './registro-diario-delete-dialog.component.html',
})
export class RegistroDiarioDeleteDialogComponent {
  registroDiario?: IRegistroDiario;

  constructor(protected registroDiarioService: RegistroDiarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.registroDiarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
