import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegistroDiarioComponent } from './list/registro-diario.component';
import { RegistroDiarioDetailComponent } from './detail/registro-diario-detail.component';
import { RegistroDiarioUpdateComponent } from './update/registro-diario-update.component';
import { RegistroDiarioDeleteDialogComponent } from './delete/registro-diario-delete-dialog.component';
import { RegistroDiarioRoutingModule } from './route/registro-diario-routing.module';

@NgModule({
  imports: [SharedModule, RegistroDiarioRoutingModule],
  declarations: [
    RegistroDiarioComponent,
    RegistroDiarioDetailComponent,
    RegistroDiarioUpdateComponent,
    RegistroDiarioDeleteDialogComponent,
  ],
  entryComponents: [RegistroDiarioDeleteDialogComponent],
})
export class RegistroDiarioModule {}
