import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoMembresiaComponent } from './list/tipo-membresia.component';
import { TipoMembresiaDetailComponent } from './detail/tipo-membresia-detail.component';
import { TipoMembresiaUpdateComponent } from './update/tipo-membresia-update.component';
import { TipoMembresiaDeleteDialogComponent } from './delete/tipo-membresia-delete-dialog.component';
import { TipoMembresiaRoutingModule } from './route/tipo-membresia-routing.module';

@NgModule({
  imports: [SharedModule, TipoMembresiaRoutingModule],
  declarations: [TipoMembresiaComponent, TipoMembresiaDetailComponent, TipoMembresiaUpdateComponent, TipoMembresiaDeleteDialogComponent],
  entryComponents: [TipoMembresiaDeleteDialogComponent],
})
export class TipoMembresiaModule {}
