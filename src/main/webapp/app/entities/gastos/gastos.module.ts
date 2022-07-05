import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GastosComponent } from './list/gastos.component';
import { GastosDetailComponent } from './detail/gastos-detail.component';
import { GastosUpdateComponent } from './update/gastos-update.component';
import { GastosDeleteDialogComponent } from './delete/gastos-delete-dialog.component';
import { GastosRoutingModule } from './route/gastos-routing.module';

@NgModule({
  imports: [SharedModule, GastosRoutingModule],
  declarations: [GastosComponent, GastosDetailComponent, GastosUpdateComponent, GastosDeleteDialogComponent],
  entryComponents: [GastosDeleteDialogComponent],
})
export class GastosModule {}
