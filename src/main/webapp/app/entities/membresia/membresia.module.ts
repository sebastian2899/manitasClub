import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MembresiaComponent } from './list/membresia.component';
import { MembresiaDetailComponent } from './detail/membresia-detail.component';
import { MembresiaUpdateComponent } from './update/membresia-update.component';
import { MembresiaDeleteDialogComponent } from './delete/membresia-delete-dialog.component';
import { MembresiaRoutingModule } from './route/membresia-routing.module';

@NgModule({
  imports: [SharedModule, MembresiaRoutingModule],
  declarations: [MembresiaComponent, MembresiaDetailComponent, MembresiaUpdateComponent, MembresiaDeleteDialogComponent],
  entryComponents: [MembresiaDeleteDialogComponent],
})
export class MembresiaModule {}
