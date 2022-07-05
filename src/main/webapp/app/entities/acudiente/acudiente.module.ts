import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AcudienteComponent } from './list/acudiente.component';
import { AcudienteDetailComponent } from './detail/acudiente-detail.component';
import { AcudienteUpdateComponent } from './update/acudiente-update.component';
import { AcudienteDeleteDialogComponent } from './delete/acudiente-delete-dialog.component';
import { AcudienteRoutingModule } from './route/acudiente-routing.module';

@NgModule({
  imports: [SharedModule, AcudienteRoutingModule],
  declarations: [AcudienteComponent, AcudienteDetailComponent, AcudienteUpdateComponent, AcudienteDeleteDialogComponent],
  entryComponents: [AcudienteDeleteDialogComponent],
})
export class AcudienteModule {}
